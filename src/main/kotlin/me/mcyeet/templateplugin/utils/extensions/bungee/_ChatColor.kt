package me.mcyeet.templateplugin.utils.extensions.bungee

import net.md_5.bungee.api.ChatColor

object _ChatColor {
    val ChatColor.char: Char
        get() = this.toString().last()

    fun ChatColor.getByChar(color: String): ChatColor {
        return ChatColor.getByChar(color[0])
    }

    fun ChatColor.getByChar(color: Char): ChatColor {
        return org.bukkit.ChatColor.getByChar(color)?.asBungee()
            ?: throw IllegalArgumentException("Unable to find ChatColor of Char '${color}'")
    }

}