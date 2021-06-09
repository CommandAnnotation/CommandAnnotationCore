package skywolf46.commandannotationmc.minecraft.registry

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import skywolf46.commandannotation.kotlin.CommandAnnotationCore
import skywolf46.commandannotation.kotlin.data.Arguments
import skywolf46.commandannotationmc.minecraft.annotations.preprocessor.ConsoleOnly
import skywolf46.extrautility.util.sendMessage

object MinecraftProcessorRegistry {

    fun register() {
        CommandAnnotationCore.registerPreprocessAnnotation(ConsoleOnly::class.java, 0) {
            params<Player> {
                return@registerPreprocessAnnotation true
            }
            params<CommandSender>()?.sendMessage(*it.value)
            return@registerPreprocessAnnotation false
        }

        CommandAnnotationCore.registerPreprocessAnnotation(ConsoleOnly::class.java, 0) {
            params<Player> { player ->
                player.sendMessage(*it.value)
                return@registerPreprocessAnnotation false
            }
            return@registerPreprocessAnnotation true
        }
    }


}