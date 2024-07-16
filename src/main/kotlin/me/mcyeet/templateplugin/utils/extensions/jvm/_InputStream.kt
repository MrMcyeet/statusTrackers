package me.mcyeet.templateplugin.utils.extensions.jvm

import java.io.File
import java.io.InputStream
import kotlin.io.path.createParentDirectories

object _InputStream {

    /**
     * Writes the content of an InputStream to a specified File.
     *
     * @param file The destination File where the InputStream content will be written.
     */
    fun InputStream.writeTo(file: File) {
        use { input ->
            file.toPath().createParentDirectories()
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }

    /**
     * Writes the content of an InputStream to a file specified by its path.
     *
     * @param file The path to the destination file where the InputStream content will be written.
     */
    fun InputStream.writeTo(file: String) {
        this.writeTo(File(file))
    }

}