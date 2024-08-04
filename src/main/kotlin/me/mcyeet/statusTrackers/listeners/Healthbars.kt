package me.mcyeet.statusTrackers.listeners

import com.github.retrooper.packetevents.event.PacketListenerAbstract
import com.github.retrooper.packetevents.event.PacketSendEvent
import com.github.retrooper.packetevents.protocol.entity.data.EntityData
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnLivingEntity
import io.github.retrooper.packetevents.util.SpigotConversionUtil
import me.mcyeet.statusTrackers.utils.extensions.bukkit._Player.sendPacket
import me.mcyeet.statusTrackers.utils.extensions.jvm._String.containsIgnoreCase
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*

class Healthbars: Listener, PacketListenerAbstract() {
    companion object {
        private fun sendHealthBarTo(player: Player, entity: LivingEntity, healthDelta: Double = 0.0) {
            sendHealthBarTo(listOf(player), entity, healthDelta)
        }

        private fun sendHealthBarToNearby(entity: LivingEntity, healthDelta: Double = 0.0) {
            val players = entity.getNearbyEntities(100.0, 400.0, 100.0).filterIsInstance<Player>()
            sendHealthBarTo(players, entity, healthDelta)
        }

        private fun sendHealthBarTo(players: Collection<Player>, entity: LivingEntity, healthDelta: Double = 0.0) {
            val currentHealth = entity.health - healthDelta
            val maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value

            val totalBars = 10
            val filledBars = ((currentHealth / maxHealth) * totalBars).toInt()

            val healthText = text(" ${String.format("%.1f", currentHealth).toDouble()}/$maxHealth ", NamedTextColor.WHITE)

            val leftFilledBars = filledBars.coerceIn(1, 5)
            val leftEmptyBars = (5 - leftFilledBars)

            val rightFilledBars = (filledBars - 5).coerceAtLeast(0)
            val rightEmptyBars = (5 - rightFilledBars)

            val leftFilledComponent = text("■".repeat(leftFilledBars), NamedTextColor.RED)
            val rightFilledComponent = text("■".repeat(rightFilledBars), NamedTextColor.RED)
            val leftEmptyComponent = text("■".repeat(leftEmptyBars), NamedTextColor.GRAY)
            val rightEmptyComponent = text("■".repeat(rightEmptyBars), NamedTextColor.GRAY)

            val name = text()
                .append(text("[", NamedTextColor.DARK_GRAY))
                .append(leftFilledComponent)
                .append(leftEmptyComponent)
                .append(healthText)
                .append(rightFilledComponent)
                .append(rightEmptyComponent)
                .append(text("]", NamedTextColor.DARK_GRAY))
                .build()

            val entityData = EntityData(2, EntityDataTypes.OPTIONAL_COMPONENT, Optional.ofNullable(AdventureSerializer.toJson(name)))
            val packet = WrapperPlayServerEntityMetadata(entity.entityId, listOf(entityData))

            players.forEach { player ->
                player.sendPacket(packet)
            }
        }
    }

    override fun onPacketSend(event: PacketSendEvent) {
        //if (!arrayOf(
        //        PacketType.Play.Server.ENTITY_RELATIVE_MOVE_AND_ROTATION,
        //        PacketType.Play.Server.ENTITY_RELATIVE_MOVE,
        //        PacketType.Play.Server.ENTITY_HEAD_LOOK,
        //        PacketType.Play.Server.ENTITY_ROTATION,
        //        PacketType.Play.Server.ENTITY_TELEPORT,
        //        PacketType.Play.Server.ENTITY_VELOCITY,
        //        PacketType.Play.Server.UNLOAD_CHUNK,
        //        PacketType.Play.Server.CHUNK_DATA
        //).contains(event.packetType))
        //    println("sending ${event.packetType.name}")

        if (event.packetType != PacketType.Play.Server.SPAWN_ENTITY)
            return

        val player = (event.player as Player)
        val packet = WrapperPlayServerSpawnEntity(event)
        val entity = (SpigotConversionUtil.getEntityById(player.world, packet.entityId) as? LivingEntity)
            ?: return

        if (arrayOf(EntityType.ENDER_DRAGON, EntityType.WITHER, EntityType.PLAYER).contains(entity.type))
            return

        if (entity.health == entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value)
            return

        event.postTasks.add { sendHealthBarTo(player, entity) }
    }

    @EventHandler
    fun onDamage(event: EntityDamageByEntityEvent) {
        val damagee = (event.entity as? LivingEntity)
            ?: return

        if (arrayOf(EntityType.ENDER_DRAGON, EntityType.WITHER, EntityType.PLAYER).contains(damagee.type))
            return

        sendHealthBarToNearby(damagee, event.damage)
    }
}