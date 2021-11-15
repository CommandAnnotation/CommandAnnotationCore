package skywolf46.commandannotationmc.minecraft.util

import org.bukkit.Bukkit
import skywolf46.commandannotation.kotlin.annotation.AutoCompleteProvider
import skywolf46.commandannotation.kotlin.data.Arguments

object MinecraftCompleterModule {
    @AutoCompleteProvider("players")
    fun Arguments.getPlayerCommands(): List<String> {
        return mutableListOf<String>().apply {
            for (x in Bukkit.getOnlinePlayers()) {
                add(x.name)
            }
        }
    }
}