package me.mcyeet.statusTrackers.utils.extensions.bukkit

//import com.github.retrooper.packetevents.PacketEvents
//import com.github.retrooper.packetevents.wrapper.PacketWrapper
import net.milkbowl.vault.permission.Permission
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import kotlin.String

object _OfflinePlayer {

    private val permissionManager = Bukkit.getServer().servicesManager.getRegistration(Permission::class.java)?.provider
        ?: throw IllegalStateException("Could not find valid PermissionManager!")

    /**
     * Checks if an OfflinePlayer has a specific permission.
     *
     * @param permission The permission string to check.
     * @return `true` if the OfflinePlayer has the specified permission, `false` otherwise.
     */
    fun OfflinePlayer.hasPerm(permission: String): Boolean {
        val ret = permissionManager.playerHas(null, this, permission)
        //println("Checking if ${this.name} has permission \"$permission\" ($ret)")
        return ret
    }

    //fun OfflinePlayer.sendPacket(packet: PacketWrapper<*>) {
    //    PacketEvents.getAPI().playerManager.sendPacket(this, packet)
    //}

}