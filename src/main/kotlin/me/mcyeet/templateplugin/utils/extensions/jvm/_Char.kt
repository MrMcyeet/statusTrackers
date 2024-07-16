package me.mcyeet.templateplugin.utils.extensions.jvm

object _Char {

    /**
     * Checks if the character contains a match for the given regular expression.
     *
     * This function determines if the character contains any substring that matches the provided regular expression.
     *
     * @param regex The regular expression pattern to check for a match.
     * @return `true` if the character contains a match for the regular expression, `false` otherwise.
     */
    fun Char.contains(regex: Regex): Boolean {
        return regex.containsMatchIn(this.toString())
    }

}