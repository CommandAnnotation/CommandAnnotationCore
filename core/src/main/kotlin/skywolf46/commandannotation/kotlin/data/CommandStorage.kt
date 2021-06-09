package skywolf46.commandannotation.kotlin.data

import skywolf46.commandannotation.kotlin.abstraction.ICommand
import skywolf46.commandannotation.kotlin.abstraction.ICommandCondition
import skywolf46.commandannotation.kotlin.impl.FixedStringCondition
import skywolf46.extrautility.data.ArgumentStorage
import java.lang.Exception
import java.lang.IllegalStateException

class CommandStorage<T : ICommand>() {
    private val map = mutableListOf<Pair<ICommandCondition, CommandStorage<T>>>()
    var boundedCommand: T? = null

    fun inspectCommand(command: String, argumentStorage: ArgumentStorage): T? {
        val args = Arguments(false, argumentStorage, command)
        return inspectCommand(args)
    }

    fun inspectCommand(arguments: Arguments): T? {
        for ((x, y) in map) {
            val iterator = arguments.iterator()
            try {
                if (x.isMatched(iterator)) {
                    arguments.increasePointer(iterator.forwardedSize())
                    return y.inspectCommand(arguments)
                }
            } catch (_: Exception) {
                // Ignored
            }
        }
        return boundedCommand
    }

    private fun getCommand(vararg args: ICommandCondition, pointer: Int): T? {
        if (args.size <= pointer)
            return boundedCommand
        for ((x, y) in map) {
            if (x == args[pointer]) {
                return y.getCommand(*args, pointer = pointer + 1)
            }
        }
        return null
    }

    private fun registerCommand(commandStart: String, command: T, pointer: Int, vararg args: ICommandCondition) {
        if (args.size <= pointer) {
            // TODO Change to list
            boundedCommand = command
            return
        }
        var condition: CommandStorage<T>? = null
        for ((x, y) in map) {
            if (x == args[pointer]) {
                condition = y
                break
            }
        }
        (condition ?: (args[pointer] to CommandStorage<T>()).apply {
            map += this
        }.second).registerCommand(commandStart, command, pointer + 1, *args)
    }

    fun getCommand(vararg args: ICommandCondition) = getCommand(*args, pointer = 0)

    fun registerCommand(command: T, commandStart: String, vararg args: ICommandCondition) {
        registerCommand(commandStart, command, 0, *args)
    }

}