package me.mcyeet.templateplugin.utils

import java.util.*

/**
 * An array list that automatically removes elements after a set amount of time.
 *
 * @param expirationTime The amount of time, in milliseconds, that an element should remain in the list before being removed.
 */
class AutoExpiringArrayList<T>(private val expirationTime: Long) : ArrayList<T>() {

    // A map that stores the expiration timestamps for each element in the list
    private val expirationTimestamps = HashMap<T, Long>()

    /**
     * Add an element to the list.
     * The element's expiration timestamp is set to the current time plus the expiration time specified when the
     * AutoExpiringArrayList was created.
     */
    override fun add(element: T): Boolean {
        checkAndRemoveExpiredElements()
        expirationTimestamps[element] = System.currentTimeMillis() + expirationTime
        return super.add(element)
    }

    /**
     * Add an element to the list at the specified index.
     * The element's expiration timestamp is set to the current time plus the expiration time specified when the
     * AutoExpiringArrayList was created.
     */
    override fun add(index: Int, element: T) {
        checkAndRemoveExpiredElements()
        expirationTimestamps[element] = System.currentTimeMillis() + expirationTime
        super.add(index, element)
    }

    /**
     * Add a collection of elements to the list.
     * The expiration timestamps for each element are set to the current time plus the expiration time specified
     * when the AutoExpiringArrayList was created.
     */
    override fun addAll(elements: Collection<T>): Boolean {
        checkAndRemoveExpiredElements()
        elements.forEach { element ->
            expirationTimestamps[element] = System.currentTimeMillis() + expirationTime
        }
        return super.addAll(elements)
    }

    /**
     * Add a collection of elements to the list at the specified index.
     * The expiration timestamps for each element are set to the current time plus the expiration time specified
     * when the AutoExpiringArrayList was created.
     */
    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        checkAndRemoveExpiredElements()
        elements.forEach { element ->
            expirationTimestamps[element] = System.currentTimeMillis() + expirationTime
        }
        return super.addAll(index, elements)
    }

    /**
     * Remove an element from the list.
     * The element's expiration timestamp is also removed from the expirationTimestamps map.
     */
    override fun remove(element: T): Boolean {
        checkAndRemoveExpiredElements()
        expirationTimestamps.remove(element)
        return super.remove(element)
    }

    /**
     * Remove a collection of elements from the list.
     * The expiration timestamps for each element are also removed from the expirationTimestamps map.
     */
    override fun removeAll(elements: Collection<T>): Boolean {
        checkAndRemoveExpiredElements()
        elements.forEach { element ->
            expirationTimestamps.remove(element)
        }
        return super.removeAll(elements)
    }

    /**
     * Remove all elements from the list.
     * The expiration timestamps for all elements are also removed from the expirationTimestamps map.
     */
    override fun clear() {
        checkAndRemoveExpiredElements()
        expirationTimestamps.clear()
        super.clear()
    }

    /**
     * Set the element at the specified index in the list.
     * The element's expiration timestamp is set to the current time plus the expiration time specified when the
     * AutoExpiringArrayList was created.
     */
    override fun set(index: Int, element: T): T {
        checkAndRemoveExpiredElements()
        expirationTimestamps[element] = System.currentTimeMillis() + expirationTime
        return super.set(index, element)
    }

    /**
     * Check if an element is contained in the list.
     */
    override fun contains(element: T): Boolean {
        checkAndRemoveExpiredElements()
        return super.contains(element)
    }

    /**
     * Get the element at the specified index in the list.
     */
    override fun get(index: Int): T {
        checkAndRemoveExpiredElements()
        return super.get(index)
    }

    /**
     * Get an iterator for the list.
     */
    override fun iterator(): MutableIterator<T> {
        checkAndRemoveExpiredElements()
        return super.iterator()
    }

    /**
     * Check for and remove any expired elements from the list.
     * An element is considered expired if its expiration timestamp, stored in the expirationTimestamps map,
     * is earlier than the current time.
     */
    private fun checkAndRemoveExpiredElements() {
        // Iterate through the expirationTimestamps map
        val iterator = expirationTimestamps.iterator()

        // If the expiration timestamp is earlier than the current time, remove the element from the map and the list
        while (iterator.hasNext()) {
            val (element, expirationTimestamp) = iterator.next()
            if (expirationTimestamp < System.currentTimeMillis()) {
                iterator.remove()
                super.remove(element)
            }
        }
    }
}