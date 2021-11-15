package skywolf46.commandannotationmc.minecraft.registry

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import skywolf46.commandannotation.kotlin.CommandAnnotationCore
import skywolf46.commandannotationmc.minecraft.annotations.preprocessor.ConsoleOnly
import skywolf46.commandannotationmc.minecraft.annotations.preprocessor.PlayerOnly
import skywolf46.commandannotationmc.minecraft.annotations.preprocessor.RequirePermission

object MinecraftProcessorRegistry {

    fun register() {
        CommandAnnotationCore.registerPreprocessAnnotation(PlayerOnly::class.java, 0) {
             params<Player> {
                return@registerPreprocessAnnotation true
            }
            return@registerPreprocessAnnotation false
        }

        CommandAnnotationCore.registerPreprocessAnnotation(ConsoleOnly::class.java, 0) {
            params<Player> {
                return@registerPreprocessAnnotation false
            }

            return@registerPreprocessAnnotation true
        }

        CommandAnnotationCore.registerPreprocessAnnotation(RequirePermission::class.java, 100) { annot ->
            params<CommandSender> {
                if (!(annot.ignoreIfOperator && it.isOp) && !it.hasPermission(annot.permission)) {
                    return@registerPreprocessAnnotation false
                }
            }
            return@registerPreprocessAnnotation true
        }
    }


}