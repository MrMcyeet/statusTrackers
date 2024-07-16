package me.mcyeet.templateplugin.utils.extensions.jvm

object _Any {

    /**
     * An extension property that allows early return without providing a value.
     *
     * This property can be used in instances where you want to exit a function early without returning any value.
     * For example, you can use `return somethingThatReturnsData().ignore` in a function that shouldn't return any value.
     */
    val Any.ignore: Unit
        get() = Unit

}