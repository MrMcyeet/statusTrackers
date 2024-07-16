package me.mcyeet.templateplugin.utils.extensions.bukkit

import org.bukkit.entity.Entity
import org.bukkit.entity.Flying
import org.bukkit.entity.Mob
import org.bukkit.entity.Monster

object _Entity {

    val Entity.isHostile: Boolean get() { return ((this is Monster) || (this is Flying)) }
    val Entity.isLeashed: Boolean get() { return if (this !is Mob) false else this.isLeashed }
    val Entity.isNamed: Boolean get() { return this.customName != null }

}