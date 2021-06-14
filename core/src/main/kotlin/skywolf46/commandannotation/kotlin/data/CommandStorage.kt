package skywolf46.commandannotation.kotlin.data

import skywolf46.commandannotation.kotlin.CommandAnnotationCore
import skywolf46.commandannotation.kotlin.abstraction.ICommand
import skywolf46.commandannotation.kotlin.abstraction.ICommandCondition
import skywolf46.commandannotation.kotlin.annotation.AutoComplete
import skywolf46.commandannotation.kotlin.impl.AbstractCommand
import skywolf46.extrautility.data.ArgumentStorage
import skywolf46.extrautility.util.PriorityReference

class CommandStorage<T : AbstractCommand>() {
    private val map = mutableListOf<Pair<ICommandCondition, CommandStorage<T>>>()
    var boundedCommand = object : ArrayList<PriorityReference<T>>() {
        override fun add(element: PriorityReference<T>): Boolean {
            val result = super.add(element)
            sort()
            return result
        }
    }

    fun inspectCommand(command: String, argumentStorage: ArgumentStorage): List<T> {
        val args = Arguments(false, argumentStorage, command)
        return inspectCommand(args)
    }

    fun inspectCommand(arguments: Arguments): List<T> {
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
        return boundedCommand.map { x -> x.data }
    }

    private fun getCommand(vararg args: ICommandCondition, pointer: Int): List<T> {
        if (args.size <= pointer)
            return boundedCommand.map { x -> x.data }
        for ((x, y) in map) {
            if (x == args[pointer]) {
                return y.getCommand(*args, pointer = pointer + 1)
            }
        }
        return emptyList()
    }

    private fun registerCommand(
        commandStart: String,
        command: PriorityReference<T>,
        pointer: Int,
        vararg args: ICommandCondition,
    ) {
        if (args.size <= pointer) {
            boundedCommand.add(command)
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

    fun registerCommand(command: PriorityReference<T>, commandStart: String, vararg args: ICommandCondition) {
        registerCommand(commandStart, command, 0, *args)
    }

    fun inspectNextParameter(prevParam: List<T>, arguments: Arguments): List<String> {
//        for ((x, y) in map) {
//            val iterator = arguments.iterator()
//            try {
//                if (x.isMatched(iterator)) {
//                    arguments.increasePointer(iterator.forwardedSize())
//                    val next = y.inspectNextParameter(boundedCommand, arguments)
//                }
//            } catch (_: Exception) {
//                // Ignored
//            }
//        }
//        val next = mutableListOf<AutoComplete>()
//        for (x in prevParam) {
//            x.findAnnotations(AutoComplete::class.java)?.forEach {
//                x.runMarked()
//                next += CommandAnnotationCore.markManager.findMarked(it.n)
//            }
//        }
//

        // TODO: 2021-06-13 Make autocomplete inspector
        return emptyList()

    }

}