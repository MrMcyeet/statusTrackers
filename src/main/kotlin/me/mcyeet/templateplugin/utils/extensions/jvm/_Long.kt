package me.mcyeet.templateplugin.utils.extensions.jvm

import java.util.concurrent.atomic.AtomicLong

object _Long {

    /**
     * Extension property to convert a Long value into an AtomicLong.
     *
     * This property allows you to easily create an AtomicLong instance from a Long value.
     *
     * @return The AtomicLong instance initialized with the given Long value.
     */
    val Long.atomic: AtomicLong
        get() = AtomicLong(this)

}