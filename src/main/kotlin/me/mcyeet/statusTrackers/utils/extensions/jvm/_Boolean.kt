package me.mcyeet.statusTrackers.utils.extensions.jvm

object _Boolean {

    /**
     * Returns the value [ifTrue] if the receiver is true, otherwise returns [ifFalse].
     *
     * @param ifTrue the value to return if the receiver is true
     * @param ifFalse the value to return if the receiver is false
     * @return the value [ifTrue] if the receiver is true, otherwise [ifFalse]
     */
    fun <T> Boolean?.yield(ifTrue: T, ifFalse: T): T {
        return if (this == true) ifTrue else ifFalse
    }

}