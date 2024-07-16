package me.mcyeet.templateplugin.example

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class Listener: Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        println("${event.player} joined :)")
    }

}