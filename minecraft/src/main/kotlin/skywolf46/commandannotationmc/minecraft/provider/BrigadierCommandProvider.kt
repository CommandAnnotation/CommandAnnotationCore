package skywolf46.commandannotationmc.minecraft.provider

import skywolf46.commandannotation.kotlin.abstraction.ICommand
import skywolf46.commandannotation.kotlin.abstraction.ICommandProvider
import skywolf46.commandannotationmc.minecraft.annotations.MinecraftCommand
import skywolf46.commandannotationmc.minecraft.impl.BrigadierCommandInstance

import skywolf46.extrautility.util.MethodInvoker

class BrigadierCommandProvider : ICommandProvider<MinecraftCommand> {
    companion object {
        fun isCompatible(): Boolean {
            return try {
                Class.forName("com.mojang.brigadier.Command")
                true
            } catch (e: Throwable) {
                false
            }
        }
    }

    override fun generateCommand(annotation: MinecraftCommand, wrapper: MethodInvoker): ICommand {
        return BrigadierCommandInstance(annotation.value, emptyArray(), wrapper, annotation.priority)
    }
}