package skywolf46.commandannotationmc.minecraft

import org.bukkit.plugin.java.JavaPlugin
import skywolf46.commandannotation.kotlin.CommandAnnotationCore
import skywolf46.commandannotation.kotlin.data.BaseCommandStartStorage
import skywolf46.commandannotation.kotlin.util.CommandInspector
import skywolf46.commandannotationmc.minecraft.annotations.MinecraftCommand
import skywolf46.commandannotationmc.minecraft.impl.MinecraftCommandInstance
import skywolf46.commandannotationmc.minecraft.impl.conditions.PlayerCondition
import skywolf46.commandannotationmc.minecraft.provider.BrigadierCommandProvider
import skywolf46.commandannotationmc.minecraft.provider.MinecraftCommandProvider
import skywolf46.commandannotationmc.minecraft.registry.MinecraftArgumentRegistry
import skywolf46.commandannotationmc.minecraft.registry.MinecraftProcessorRegistry
import skywolf46.extrautility.annotations.AllowScanning
import skywolf46.extrautility.util.MinecraftLoader
import skywolf46.extrautility.util.log
import skywolf46.extrautility.util.schedule

@AllowScanning
class CommandAnnotation : JavaPlugin() {
    companion object {
        val command = BaseCommandStartStorage<MinecraftCommandInstance>()
    }

    override fun onEnable() {
        if (BrigadierCommandProvider.isCompatible()) {
            log("§bCommandAnnotation §7| §7Using brigadier command provider.")
            CommandAnnotationCore.registerCommandProvider(MinecraftCommand::class.java, BrigadierCommandProvider())
        } else {
            log("§bCommandAnnotation §7| §7Using default command provider.")
            CommandAnnotationCore.registerCommandProvider(MinecraftCommand::class.java, MinecraftCommandProvider())
        }
        MinecraftProcessorRegistry.register()
        MinecraftArgumentRegistry.register()
        registerMinecraftCondition()
        schedule {
            log("§bCommandAnnotation §7| §eScanning classes")
            val elapsed = System.currentTimeMillis()
            CommandAnnotationCore.scanAllClass(MinecraftLoader.loadAllClass())
            log("§bCommandAnnotation §7| §eClass scanning completed in ${System.currentTimeMillis() - elapsed}ms")
        }
    }

    private fun registerMinecraftCondition() {
        CommandInspector.registerCondition("player", PlayerCondition(true))
    }
}
