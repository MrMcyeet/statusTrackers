package me.mcyeet.statusTrackers.utils.extensions.bukkit

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.wrapper.PacketWrapper
import me.mcyeet.statusTrackers.StatusTrackers.Companion.Plugin
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object _Player {

    fun Player.sendPackets(packets: Collection<PacketWrapper<*>>) {
        packets.forEach { this.sendPacket(it) }
    }

    fun Player.sendPacket(packet: PacketWrapper<*>) {
        Bukkit.getScheduler().runTaskAsynchronously(Plugin, Runnable {
            PacketEvents.getAPI().playerManager.sendPacket(this, packet)
        })
    }

    fun Player.sendActionBar(message: String) {
        this.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
    }

    fun Player.sendMessage(message: BaseComponent) {
        this.spigot().sendMessage(message)
    }

    fun Player.sendMessage(vararg components: BaseComponent) {
        this.spigot().sendMessage(*components)
    }

}