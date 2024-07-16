package me.mcyeet.templateplugin.utils.extensions.jvm

import kotlin.collections.Collection

object _Collection {

    /**
     * Checks if the collection contains any elements that are present in another collection.
     *
     * This function checks if any element from the current collection is contained in the 'other' collection by comparing
     * their string representations.
     *
     * @param other The collection to check for shared elements.
     * @return `true` if there are common elements between the two collections, `false` otherwise.
     */
    fun <T> Collection<T>.containsAny(other: Collection<T>): Boolean {
        return this.any { element1 -> other.any { element2 -> element2.toString().contains(element1.toString()) } }
    }

}