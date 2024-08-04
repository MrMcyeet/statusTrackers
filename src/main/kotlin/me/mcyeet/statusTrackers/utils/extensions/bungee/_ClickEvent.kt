package me.mcyeet.statusTrackers.utils.extensions.bungee

import net.md_5.bungee.api.chat.ClickEvent

object _ClickEvent {

    // Extension constructors?
    fun openUrl(url: String): ClickEvent {
        return ClickEvent(ClickEvent.Action.OPEN_URL, url)
    }

    fun suggestCommand(command: String): ClickEvent {
        return ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)
    }

    fun runCommand(command: String): ClickEvent {
        return ClickEvent(ClickEvent.Action.RUN_COMMAND, command)
    }

    fun copy(text: String): ClickEvent {
        return ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, text)
    }

    /**
     * idfk if or how this works?
     */
    fun changePage(page: String): ClickEvent {
        return ClickEvent(ClickEvent.Action.CHANGE_PAGE, page)
    }

    /**
     * idfk if or how this works?
     */
    fun changePage(page: Int): ClickEvent {
        return ClickEvent(ClickEvent.Action.CHANGE_PAGE, "$page")
    }

}