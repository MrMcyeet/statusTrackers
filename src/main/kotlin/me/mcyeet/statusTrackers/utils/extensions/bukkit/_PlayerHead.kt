package me.mcyeet.statusTrackers.utils.extensions.bukkit

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object _PlayerHead {

    /**
     * Creates an ItemStack representing a player head with a custom texture from a URL.
     *
     * This function generates a player head ItemStack with a custom texture specified by the provided URL.
     *
     * @param url The URL of the custom texture to use for the player head.
     * @return An ItemStack representing a player head with the custom texture.
     */
    fun fromURL(url: String): ItemStack {
        return Bukkit.getUnsafe().modifyItemStack(ItemStack(Material.PLAYER_HEAD), "{SkullOwner:{Id:\"00000000-0000-0000-0000-000000000000\",Properties:{textures:[{Value:\"$url\"}]}}}")
    }

}