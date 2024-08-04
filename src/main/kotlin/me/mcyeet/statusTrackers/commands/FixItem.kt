package me.mcyeet.statusTrackers.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandArguments
import me.mcyeet.statusTrackers.utils.Command
import org.bukkit.entity.Player

object FixItem: Command {
    override val command = CommandAPICommand("fixItem")
        .executesPlayer(FixItem::run)

    private fun run(sender: Player, args: CommandArguments) {
        sender.inventory.itemInMainHand.lore(null)
        sender.sendMessage("§aOk!")
    }
}