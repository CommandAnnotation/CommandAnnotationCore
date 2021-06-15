package skywolf46.commandannotationmc.minecraft.provider

import skywolf46.commandannotation.kotlin.abstraction.ICommand
import skywolf46.commandannotation.kotlin.abstraction.ICommandProvider
import skywolf46.commandannotationmc.minecraft.annotations.MinecraftCommand
import skywolf46.commandannotationmc.minecraft.impl.MinecraftCommandInstance
import skywolf46.extrautility.util.MethodInvoker

class MinecraftCommandProvider : ICommandProvider<MinecraftCommand> {
    override fun generateCommand(annotation: MinecraftCommand, wrapper: MethodInvoker): ICommand {
        return MinecraftCommandInstance(annotation.values, emptyArray(), wrapper, annotation.priority)
    }
}