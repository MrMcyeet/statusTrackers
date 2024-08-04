package me.mcyeet.statusTrackers

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.event.PacketListenerAbstract
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import dev.jorel.commandapi.CommandAPICommand
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder
import me.mcyeet.statusTrackers.utils.Command
import me.mcyeet.statusTrackers.utils.YamlDocument
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import java.io.File
import java.io.FileNotFoundException

class StatusTrackers: JavaPlugin() {
    companion object {
        lateinit var Config: YamlDocument
        lateinit var Plugin: JavaPlugin

        private var LastConfigReload = 0L
        private val ConfigReloadTask by lazy {
            Bukkit.getScheduler().runTaskTimerAsynchronously(Plugin, Runnable {
                val defaultConfig = Plugin.getResource("config.yml") ?: throw FileNotFoundException("Default config is either missing or unreadable")
                val configFile = File(Plugin.dataFolder, "config.yml")

                val configExists = configFile.exists()
                val configModified = configFile.lastModified() != LastConfigReload

                // If the config both exists and hasn't been modified, nothing needs done
                if (configExists && !configModified)
                    return@Runnable

                Plugin.logger.info("Detected configuration change, reloading...")

                Config = YamlDocument.withDefault(configFile, defaultConfig)
                LastConfigReload = configFile.lastModified()

                Plugin.logger.info("Reload successful.")
            }, 5L, 300L)
        }
    }

    override fun onLoad() {
        Plugin = this

        //Load PacketEvents
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(Plugin))
        PacketEvents.getAPI().settings.checkForUpdates(false)

        //Load CommandAPI
        CommandAPI.onLoad(CommandAPIBukkitConfig(Plugin).silentLogs(true))
    }

    override fun onEnable() {
        if (!ConfigReloadTask.isCancelled) {
            logger.info("Config auto-reload enabled!")
            logger.info("This plugin will automatically detect and reload when you make configuration changes.")
        }

        val reflections = Reflections(this.classLoader)
        reflections.getSubTypesOf(Listener::class.java).apply {
            this.forEach { event ->
                val eventClass = event.getDeclaredConstructor().newInstance()
                Bukkit.getPluginManager().registerEvents(eventClass, Plugin)
            }

            logger.info("Successfully registered $size events!")
        }

        val commands = reflections.getSubTypesOf(Command::class.java).map {
            (it.getDeclaredField("INSTANCE").get(null) as Command)
                .command
        }

        CommandAPICommand("statustrackers")
            .withSubcommands(*commands.toTypedArray())
            .register(Plugin)
        logger.info("Successfully registered ${commands.size} commands!")

        reflections.getSubTypesOf(PacketListenerAbstract::class.java).apply {
            this.forEach {
                val eventClass = it.getDeclaredConstructor().newInstance()
                PacketEvents.getAPI().eventManager.registerListener(eventClass)
            }

            Plugin.logger.info("Successfully registered $size packet listeners!")
        }

        PacketEvents.getAPI().init()
    }

    override fun onDisable() {
        ConfigReloadTask.cancel()

        //Terminate PacketEvents
        PacketEvents.getAPI().terminate()

        //Terminate CommandAPI
        //CommandAPI.onDisable()
    }
}
