package me.mcyeet.statusTrackers.utils.extensions.bungee

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import kotlin.String

object _ComponentBuilder {

    fun ComponentBuilder.append(component: ComponentBuilder): ComponentBuilder {
        return this.append(component.create())
    }

    fun ComponentBuilder.openUrl(url: String): ComponentBuilder {
        val clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, url)
        return this.event(clickEvent)
    }

    fun ComponentBuilder.suggestCommand(command: String): ComponentBuilder {
        val clickEvent = ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)
        return this.event(clickEvent)
    }

    fun ComponentBuilder.runCommand(command: String): ComponentBuilder {
        val clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, command)
        return this.event(clickEvent)
    }

    fun ComponentBuilder.changePage(page: String): ComponentBuilder {
        val clickEvent = ClickEvent(ClickEvent.Action.CHANGE_PAGE, page)
        return this.event(clickEvent)
    }

    fun ComponentBuilder.changePage(page: Int): ComponentBuilder {
        val clickEvent = ClickEvent(ClickEvent.Action.CHANGE_PAGE, page.toString())
        return this.event(clickEvent)
    }

    fun ComponentBuilder.copyToClipboard(text: String): ComponentBuilder {
        val clickEvent = ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, text)
        return this.event(clickEvent)
    }

    fun ComponentBuilder.hoverText(text: String): ComponentBuilder {
        val hoverText = ComponentBuilder(text).create()
        val hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText)
        return this.event(hoverEvent)
    }

    fun ComponentBuilder.hoverText(text: Array<out BaseComponent>): ComponentBuilder {
        val hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, text)
        return this.event(hoverEvent)
    }

}