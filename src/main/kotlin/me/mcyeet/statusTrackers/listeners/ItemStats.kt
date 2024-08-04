package me.mcyeet.statusTrackers.listeners

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent
import com.github.retrooper.packetevents.event.PacketListenerAbstract
import com.github.retrooper.packetevents.event.PacketSendEvent
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemAttributeModifiers.Modifier
import com.github.retrooper.packetevents.protocol.item.ItemStack
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon
import com.github.retrooper.packetevents.protocol.player.DiggingAction
import com.github.retrooper.packetevents.resources.ResourceLocation
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowItems
import io.github.retrooper.packetevents.util.SpigotConversionUtil
import me.mcyeet.statusTrackers.StatusTrackers.Companion.Plugin
import me.mcyeet.statusTrackers.utils.extensions.bukkit._Player.sendPacket
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentOffer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.enchantment.PrepareItemEnchantEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.event.player.PlayerGameModeChangeEvent
import org.bukkit.persistence.PersistentDataType
import org.reflections.Reflections
import java.util.concurrent.CountDownLatch

class ItemStats: Listener, PacketListenerAbstract() {
    override fun onPacketSend(event: PacketSendEvent) {
        if ((event.player as? Player)?.gameMode != GameMode.SURVIVAL)
            return

        fun addItemStats(item: ItemStack): ItemStack {
            val bukkitItem = SpigotConversionUtil.toBukkitItemStack(item)
            val loreModifications = mutableListOf<Component>()

            val pickaxes = arrayOf(Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.GOLDEN_PICKAXE, Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE)
            val shovels = arrayOf(Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.GOLDEN_SHOVEL, Material.IRON_SHOVEL, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL)
            val swords = arrayOf(Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.GOLDEN_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD)
            val axes = arrayOf(Material.WOODEN_AXE, Material.STONE_AXE, Material.GOLDEN_AXE, Material.IRON_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE)
            val hoes = arrayOf(Material.WOODEN_HOE, Material.STONE_HOE, Material.GOLDEN_HOE, Material.IRON_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE)

            val tools = arrayOf(*pickaxes, *shovels, *axes/*, *hoes*/)
            val projectileWeapons = arrayOf(Material.BOW, Material.CROSSBOW)
            val weapons = arrayOf(Material.TRIDENT, *projectileWeapons, *swords, *axes)


            val blocksBroken = bukkitItem.itemMeta.persistentDataContainer
                .getOrDefault(NamespacedKey(Plugin, "blockBroken"), PersistentDataType.INTEGER, 0)

            if (tools.contains(bukkitItem.type)) {
                loreModifications.add(Component.text("Blocks broken: $blocksBroken"))
            }

            val blocksTilled = bukkitItem.itemMeta.persistentDataContainer
                .getOrDefault(NamespacedKey(Plugin, "blocksTilled"), PersistentDataType.INTEGER, 0)

            if (hoes.contains(bukkitItem.type)) {
                loreModifications.add(Component.text("Grass tilled: $blocksTilled"))
            }

            val arrowsShot = bukkitItem.itemMeta.persistentDataContainer
                .getOrDefault(NamespacedKey(Plugin, "arrowsShot"), PersistentDataType.INTEGER, 0)

            if (projectileWeapons.contains(bukkitItem.type)) {
                loreModifications.add(Component.text("Arrows shot: $arrowsShot"))
            }

            val kills = bukkitItem.itemMeta.persistentDataContainer
                .getOrDefault(NamespacedKey(Plugin, "kills"), PersistentDataType.INTEGER, 0)

            if (weapons.contains(bukkitItem.type)) {
                loreModifications.add(Component.text("Kills: $kills"))
            }


            if (loreModifications.isEmpty())
                return item


            loreModifications.add(0, Component.text(""))

            val newLore = listOf(
                *(bukkitItem.lore()?.toTypedArray() ?: emptyArray()),
                *loreModifications.toTypedArray()
            )

            //bukkitItem.lore(newLore)
            return SpigotConversionUtil.fromBukkitItemStack(bukkitItem.apply { this.lore(newLore) })
        }

        when (event.packetType) {
            PacketType.Play.Server.WINDOW_ITEMS -> {
                val packet = WrapperPlayServerWindowItems(event)

                for (i in packet.items.indices) {
                    val item = packet.items[i]
                    if (item.type == ItemTypes.AIR)
                        continue

                    packet.items[i] = addItemStats(item)
                }
            }

            PacketType.Play.Server.SET_SLOT -> {
                val packet = WrapperPlayServerSetSlot(event)
                if (packet.item.type == ItemTypes.AIR)
                    return

                packet.item = addItemStats(packet.item)
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    // Since in the packet listener where we modify the lore for the items in the player's inventory we don't modify anything if they're in creative mode
    // If we simply refresh their inventory manually, when they're in creative mode, they won't generate new items when moving things in their inventory
    fun onGamemodeChange(event: PlayerGameModeChangeEvent) {
        val emptyInventory = WrapperPlayServerWindowItems(0, 1, emptyList(), ItemStack.EMPTY)
        event.player.sendPacket(emptyInventory)

        Bukkit.getScheduler().runTaskLater(Plugin, Runnable { event.player.updateInventory() }, 1L)
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val item = event.player.inventory.itemInMainHand

        val pickaxes = arrayOf(Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.GOLDEN_PICKAXE, Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE)
        val shovels = arrayOf(Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.GOLDEN_SHOVEL, Material.IRON_SHOVEL, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL)
        val axes = arrayOf(Material.WOODEN_AXE, Material.STONE_AXE, Material.GOLDEN_AXE, Material.IRON_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE)
        val tools = arrayOf(*pickaxes, *shovels, *axes/*, *hoes*/)

        if (!tools.contains(item.type))
            return

        val key = NamespacedKey(Plugin, "blockBroken")
        val blocksBroken = item.itemMeta.persistentDataContainer.getOrDefault(key, PersistentDataType.INTEGER, 0) +1

        //item.itemMeta.persistentDataContainer.set(key, PersistentDataType.INTEGER, blocksBroken)
        item.itemMeta = item.itemMeta.apply { this.persistentDataContainer.apply { set(key, PersistentDataType.INTEGER, blocksBroken) } }
    }

    @EventHandler
    fun onTill(event: BlockPlaceEvent) {
        val hoes = arrayOf(Material.WOODEN_HOE, Material.STONE_HOE, Material.GOLDEN_HOE, Material.IRON_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE)
        if (!hoes.contains(event.player.inventory.itemInMainHand.type) || (event.blockPlaced.type != Material.FARMLAND))
            return

        val item = event.player.inventory.itemInMainHand
        val key = NamespacedKey(Plugin, "blocksTilled")
        val blocksBroken = item.itemMeta.persistentDataContainer.getOrDefault(key, PersistentDataType.INTEGER, 0) +1

        //item.itemMeta.persistentDataContainer.set(key, PersistentDataType.INTEGER, blocksBroken)
        item.itemMeta = item.itemMeta.apply { this.persistentDataContainer.apply { set(key, PersistentDataType.INTEGER, blocksBroken) } }
    }

    @EventHandler
    fun onKill(event: EntityDeathEvent) {
        val killer = event.entity.killer ?: return
        val item = killer.inventory.itemInMainHand

        val swords = arrayOf(Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.GOLDEN_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD)
        val axes = arrayOf(Material.WOODEN_AXE, Material.STONE_AXE, Material.GOLDEN_AXE, Material.IRON_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE)
        val projectileWeapons = arrayOf(Material.BOW, Material.CROSSBOW)
        val weapons = arrayOf(Material.TRIDENT, *projectileWeapons, *swords, *axes)

        if (!weapons.contains(item.type))
            return

        val key = NamespacedKey(Plugin, "kills")
        val blocksBroken = item.itemMeta.persistentDataContainer.getOrDefault(key, PersistentDataType.INTEGER, 0) +1

        //item.itemMeta.persistentDataContainer.set(key, PersistentDataType.INTEGER, blocksBroken)
        item.itemMeta = item.itemMeta.apply { this.persistentDataContainer.apply { set(key, PersistentDataType.INTEGER, blocksBroken) } }
    }

    @EventHandler
    fun onArrowFire(event: EntityShootBowEvent) {
        if (event.entity !is Player)
            return

        val item = event.bow
            ?: return

        val key = NamespacedKey(Plugin, "arrowsShot")
        val blocksBroken = item.itemMeta.persistentDataContainer.getOrDefault(key, PersistentDataType.INTEGER, 0) +1

        //item.itemMeta.persistentDataContainer.set(key, PersistentDataType.INTEGER, blocksBroken)
        item.itemMeta = item.itemMeta.apply { this.persistentDataContainer.apply { set(key, PersistentDataType.INTEGER, blocksBroken) } }
    }
}