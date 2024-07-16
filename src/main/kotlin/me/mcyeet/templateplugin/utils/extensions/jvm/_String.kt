package me.mcyeet.templateplugin.utils.extensions.jvm

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import kotlin.String

object _String {

    /**
     * Sets Minecraft color codes and translates '&' color codes in the string.
     *
     * @return The string with Minecraft color codes applied.
     */
    fun String.setColors(): String {
        var message = this
        if (Bukkit.getServer().bukkitVersion.split("-")[0].split(".")[1].toInt() >= 16) {
            message = message.replace("#[a-fA-F0-9]{6}".toRegex()) { "§x${it.value.split("").joinToString("§").replace("^..|.$".toRegex(), "")}" }
        }
        return ChatColor.translateAlternateColorCodes('&', message)
    }

    /**
     * Checks if the string contains another string (case-insensitive).
     *
     * @param other The string to check for containment.
     * @return `true` if the string contains the specified string (case-insensitive), `false` otherwise.
     */
    fun String.containsIgnoreCase(other: String): Boolean {
        return this.lowercase().contains(other.lowercase())
    }

    /**
     * Escapes regex special characters in the string.
     *
     * @return The string with regex special characters escaped.
     */
    fun String.escapeRegex(): String {
        val regexSpecialChars = Regex("[{}()\\[\\].+*?^$\\\\|]")
        return regexSpecialChars.replace(this) { "\\" + it.value }
    }

}