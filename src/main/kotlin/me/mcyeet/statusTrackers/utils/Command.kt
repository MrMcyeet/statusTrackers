package me.mcyeet.statusTrackers.utils

import dev.jorel.commandapi.CommandAPICommand

//I just use this to find commands reflectively, really
interface Command {
    val command: CommandAPICommand
}