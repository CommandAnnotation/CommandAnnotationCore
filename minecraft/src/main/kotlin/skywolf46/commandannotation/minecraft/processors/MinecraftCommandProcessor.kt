package skywolf46.commandannotation.minecraft.processors

import skywolf46.commandannotation.kotlin.abstraction.ICommand
import skywolf46.commandannotation.kotlin.abstraction.ICommandProvider
import skywolf46.commandannotation.minecraft.annotations.MinecraftCommand
import skywolf46.extrautility.util.MethodWrapper

class MinecraftCommandProcessor : ICommandProvider<MinecraftCommand>{
    override fun generateCommand(annotation: MinecraftCommand, wrapper: MethodWrapper): ICommand {
        TODO("Not yet implemented")
    }
}