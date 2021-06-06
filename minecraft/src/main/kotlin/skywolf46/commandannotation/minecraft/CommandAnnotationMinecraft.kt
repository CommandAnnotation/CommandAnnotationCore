package skywolf46.commandannotation.minecraft

import org.bukkit.plugin.java.JavaPlugin
import skywolf46.commandannotation.kotlin.CommandAnnotation
import skywolf46.extrautility.util.schedule

class CommandAnnotationMinecraft : JavaPlugin() {
    override fun onEnable() {
        schedule {
            CommandAnnotation.scanAllClass()
        }
    }
}