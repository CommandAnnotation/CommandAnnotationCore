package skywolf46.commandannotationmc.minecraft.impl

import skywolf46.commandannotation.kotlin.abstraction.ICommandCondition
import skywolf46.commandannotation.kotlin.data.BasicCompleterModule
import skywolf46.commandannotation.kotlin.impl.AbstractCommand
import skywolf46.commandannotationmc.minecraft.CommandAnnotation
import skywolf46.commandannotationmc.minecraft.registry.compatibility.SuggestionRegistry
import skywolf46.commandannotationmc.minecraft.util.MinecraftCommandInjector
import skywolf46.extrautility.util.MethodInvoker
import skywolf46.extrautility.util.PriorityReference

class BrigadierCommandInstance(
    command: Array<out String>,
    condition: Array<out ICommandCondition>, wrapper: MethodInvoker, priority: Int,
) :
    AbstractCommand(command, condition, wrapper, priority) {
    lateinit var literal: SuggestionRegistry.RemappedCommandLiteral
    override fun onBaseCommandRegister(commandStart: String, vararg condition: ICommandCondition) {
        literal = SuggestionRegistry.RemappedCommandLiteral(
            if (commandStart.startsWith("/"))
                commandStart.substring(1)
            else
                commandStart
        ).register()
        MinecraftCommandInjector.inject(commandStart)
    }

    override fun onCommandRegister(commandStart: String, vararg condition: ICommandCondition) {
        CommandAnnotation.command.register(PriorityReference(MinecraftCommandInstance(arrayOf(commandStart),
            condition,
            wrapper,
            priority), priority),
            commandStart,
            *condition)
    }

    override fun onBaseCommandUnregister(commandStart: String, vararg condition: ICommandCondition) {
        SuggestionRegistry.unregister(commandStart)
    }

    override fun onCommandUnregister(commandStart: String, vararg condition: ICommandCondition) {
        literal.unregister()
    }

    override fun getMethod(): MethodInvoker {
        return wrapper
    }

    override fun bindCompleter(wrapper: BasicCompleterModule) {
        //
    }

    override fun getCompleter(): BasicCompleterModule? {
        return null
    }
}