package skywolf46.commandannotationmc.minecraft.util

import skywolf46.commandannotationmc.minecraft.impl.MinecraftCommandInstance
import skywolf46.commandannotationmc.minecraft.impl.minecraft.MinecraftCommandImpl

object MinecraftCommandInjector {
    private val commands = MinecraftCommandExtractor.parseCommandMap()
    fun inject(commandStart: String) {
        commands!![commandStart.filterCommand()] = MinecraftCommandImpl()
    }

    fun uninject(commandStart: String) {
        commands!!.remove(commandStart.filterCommand())
    }

    private fun String.filterCommand() = if (startsWith("/")) substring(1) else this
}