package me.mcyeet.templateplugin.example

//import dev.jorel.commandapi.CommandAPICommand
//import dev.jorel.commandapi.arguments.PlayerArgument
//import dev.jorel.commandapi.executors.CommandArguments
import me.mcyeet.templateplugin.utils.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object Command: Command {
    override fun register() {
        //CommandAPICommand("example")
        //    .withArguments(PlayerArgument("player"))
        //    .executes(::run)
        //    .register()
    }

    //private fun run(sender: CommandSender, args: CommandArguments) {
    //    val player = (args.get("player") as Player)
    //    sender.sendMessage("§e${player.displayName} §ais level §e${player.level}§a!")
    //}
}