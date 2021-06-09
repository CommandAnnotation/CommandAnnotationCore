package skywolf46.commandannotationmc.minecraft

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import skywolf46.commandannotation.kotlin.CommandAnnotation
import skywolf46.commandannotation.kotlin.data.Arguments
import skywolf46.commandannotation.kotlin.data.BaseCommandStartStorage
import skywolf46.commandannotationmc.minecraft.annotations.MinecraftCommand
import skywolf46.commandannotationmc.minecraft.impl.MinecraftCommandInstance
import skywolf46.commandannotationmc.minecraft.provider.MinecraftCommandProvider
import skywolf46.extrautility.util.MinecraftLoader
import skywolf46.extrautility.util.log
import skywolf46.extrautility.util.schedule

class CommandAnnotation : JavaPlugin() {
    companion object {
        val command = BaseCommandStartStorage<MinecraftCommandInstance>()
    }

    override fun onEnable() {
        CommandAnnotation.registerCommandProvider(MinecraftCommand::class.java, MinecraftCommandProvider())
        schedule {
            log("§bCommandAnnotation §7| §eScanning classes")
            val elapsed = System.currentTimeMillis()
            CommandAnnotation.scanAllClass(MinecraftLoader.loadAllClass())
            log("§bCommandAnnotation §7| §bClass scanning completed in ${System.currentTimeMillis() - elapsed}ms")
        }
    }

}