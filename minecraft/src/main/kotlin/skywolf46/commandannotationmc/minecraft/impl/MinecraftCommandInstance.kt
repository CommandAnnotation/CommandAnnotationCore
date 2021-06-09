package skywolf46.commandannotationmc.minecraft.impl

import skywolf46.commandannotation.kotlin.abstraction.ICommand
import skywolf46.commandannotation.kotlin.abstraction.ICommandCondition
import skywolf46.commandannotation.kotlin.data.Arguments
import skywolf46.commandannotation.kotlin.util.CommandInspector
import skywolf46.commandannotationmc.minecraft.CommandAnnotation
import skywolf46.commandannotationmc.minecraft.util.MinecraftCommandInjector
import skywolf46.extrautility.data.ArgumentStorage
import skywolf46.extrautility.util.MethodInvoker

class MinecraftCommandInstance(private val command: Array<out String>, private val wrapper: MethodInvoker) : ICommand {

    companion object {
        private val completeMap = mutableMapOf<String, Int>()
    }

    private var inspectedSize = 0

    init {
        if (command.size == 1)
            inspectedSize = CommandInspector.inspect(command[0]).size - 1
    }

    override fun getRawCommand(): Array<out String> {
        return command
    }

    override fun invoke(storage: Arguments) {
        storage._sysPointer = inspectedSize
        wrapper.invoke(storage._storage)
    }

    override fun onUnregister(commandStart: String, vararg condition: ICommandCondition) {

        completeMap[commandStart]?.apply {
            if (this <= 1) {
                completeMap.remove(commandStart)
                MinecraftCommandInjector.uninject(commandStart)
            } else {
                completeMap[commandStart] = (this - 1)
            }
        }
    }

    override fun onRegister(commandStart: String, vararg condition: ICommandCondition) {
        if (!completeMap.containsKey(commandStart))
            MinecraftCommandInjector.inject(commandStart)
        completeMap[commandStart] = (completeMap[commandStart] ?: 0) + 1
        CommandAnnotation.command.register(MinecraftCommandInstance(arrayOf(commandStart), wrapper),
            commandStart,
            *condition)
    }


}