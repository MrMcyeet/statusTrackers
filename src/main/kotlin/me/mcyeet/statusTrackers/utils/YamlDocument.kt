package me.mcyeet.statusTrackers.utils

import com.google.gson.Gson
import me.mcyeet.statusTrackers.utils.extensions.jvm._InputStream.writeTo
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.InputStream

/**
 * Represents a YAML document with methods for accessing and manipulating its content.
 *
 * @param configuration The input stream containing the YAML configuration data.
 * @constructor Creates a YamlDocument instance from an InputStream.
 */
class YamlDocument private constructor(private val configuration: InputStream) {
    companion object {
        private val gson = Gson()
        private val yaml = Yaml()

        /**
         * Creates a YamlDocument instance from an InputStream.
         *
         * @param config The input stream containing the YAML configuration data.
         * @return A YamlDocument instance.
         */
        fun from(config: InputStream): YamlDocument {
            return YamlDocument(config)
        }

        /**
         * Creates a YamlDocument instance from a File.
         *
         * @param configFile The File containing the YAML configuration data.
         * @return A YamlDocument instance.
         */
        fun from(configFile: File): YamlDocument {
            return YamlDocument(configFile.inputStream())
        }

        /**
         * Creates a YamlDocument instance from JSON.
         *
         * @param json The JSON string representing the YAML configuration data.
         * @return A YamlDocument instance.
         */
        fun from(json: String): YamlDocument {
            val jsonMap = gson.fromJson(json, Map::class.java)
            return YamlDocument(yaml.dump(jsonMap).byteInputStream())
        }

        /**
         * Creates a YamlDocument instance with a default configuration if the specified file doesn't exist.
         *
         * @param configFile The File containing the YAML configuration data.
         * @param defaultConfig The default configuration to use if the specified file doesn't exist.
         * @return A YamlDocument instance.
         */
        fun withDefault(configFile: File, defaultConfig: InputStream): YamlDocument {
            if (!configFile.exists())
                defaultConfig.writeTo(configFile)

            return YamlDocument(configFile.inputStream())
        }
    }

    // The configuration data loaded from the InputStream.
    private val configData = configuration.use {
        //(yaml.load(it) as MutableMap<String, Any?>)
        yaml.load<MutableMap<String, *>>(it)
    }

    /**
     * Retrieves a non-null value from the configuration data at the specified path.
     *
     * @param path The path to the value within the configuration data.
     * @throws NoSuchElementException if the specified path does not exist in the configuration.
     * @throws IllegalStateException if the retrieved value is null.
     * @return The non-null value at the specified path, casted to the requested type.
     */
    fun <T> get(path: String): T {
        return this.getNullable(path)
            ?: throw IllegalStateException("The value provided for YamlDocument key \"$path\" must not be null.")
    }

    /**
     * Retrieves a nullable value from the configuration data at the specified path.
     *
     * @param path The path to the value within the configuration data.
     * @throws NoSuchElementException if the specified path does not exist in the configuration.
     * @return The nullable value at the specified path, casted to the requested type.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getNullable(path: String): T {
        return path.split('.').fold(configData) { config: Any?, key ->
            if (!(config as Map<String, Any?>).contains(key))
                throw NoSuchElementException("Could not find expected YamlDocument key \"$path\".")

            config[key]
        } as T
    }

    /**
     * Sets a value at the specified path in the configuration data (not yet implemented).
     *
     * @param path The path where the value should be set.
     * @param value The value to set.
     * @throws NotImplementedError This method is not yet implemented.
     */
    fun set(path: String, value: Any) {
        throw NotImplementedError("This method has not yet been implemented.")
    }
}