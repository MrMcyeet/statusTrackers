package me.mcyeet.statusTrackers.utils.extensions.bukkit

import org.bukkit.Location

object _Location {

    /**
     * Adds a specified distance to the X-coordinate of the Location.
     *
     * @param distance The distance to add to the X-coordinate.
     * @return The updated Location with the specified distance added to the X-coordinate.
     */
    fun Location.addX(distance: Double): Location {
        return this.add(distance, 0.0, 0.0)
    }

    /**
     * Adds a specified distance to the Y-coordinate of the Location.
     *
     * @param distance The distance to add to the Y-coordinate.
     * @return The updated Location with the specified distance added to the Y-coordinate.
     */
    fun Location.addY(distance: Double): Location {
        return this.add(0.0, distance, 0.0)
    }

    /**
     * Adds a specified distance to the Z-coordinate of the Location.
     *
     * @param distance The distance to add to the Z-coordinate.
     * @return The updated Location with the specified distance added to the Z-coordinate.
     */
    fun Location.addZ(distance: Double): Location {
        return this.add(0.0, 0.0, distance)
    }

}