package skywolf46.commandannotationmc.minecraft.impl

import skywolf46.commandannotation.kotlin.abstraction.ICommandCondition
import skywolf46.commandannotation.kotlin.impl.AbstractCommand
import skywolf46.commandannotationmc.minecraft.CommandAnnotation
import skywolf46.commandannotationmc.minecraft.util.MinecraftCommandInjector
import skywolf46.extrautility.util.MethodInvoker

class MinecraftCommandInstance(command: Array<out String>, wrapper: MethodInvoker) :
    AbstractCommand(command, wrapper) {

    override fun onBaseCommandRegister(commandStart: String, vararg condition: ICommandCondition) {
        MinecraftCommandInjector.inject(commandStart)
    }

    override fun onCommandRegister(commandStart: String, vararg condition: ICommandCondition) {
        CommandAnnotation.command.register(MinecraftCommandInstance(arrayOf(commandStart), wrapper),
            commandStart,
            *condition)
    }

    override fun onBaseCommandUnregister(commandStart: String, vararg condition: ICommandCondition) {
        MinecraftCommandInjector.uninject(commandStart)
    }

    override fun onCommandUnregister(commandStart: String, vararg condition: ICommandCondition) {
        // Yay
    }


}