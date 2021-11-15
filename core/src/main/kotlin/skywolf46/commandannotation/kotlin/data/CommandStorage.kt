package skywolf46.commandannotation.kotlin.data

import skywolf46.commandannotation.kotlin.abstraction.ICommandCondition
import skywolf46.commandannotation.kotlin.annotation.AutoComplete
import skywolf46.commandannotation.kotlin.impl.AbstractCommand
import skywolf46.commandannotation.kotlin.impl.FixedStringCondition
import skywolf46.commandannotation.kotlin.impl.FreeArgCondition
import skywolf46.extrautility.data.ArgumentStorage
import skywolf46.extrautility.util.PriorityReference

class CommandStorage<T : AbstractCommand>(val currentCondition: ICommandCondition = FreeArgCondition) {
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
        return inspect(args, true)
    }

    fun inspect(args: Arguments, skipArgs: Boolean): List<T> {
        val matched = mutableListOf<Pair<Int, List<PriorityReference<T>>>>()
        // Return as map.
        if (currentCondition.checkMatched(args) && currentCondition.checkLastCountMatched(args, 0))
            matched += 0 to boundedCommand
        inspect(args.increasePointer(true, 0), matched, 0)
        if (matched.isEmpty())
            return emptyList()
        return matched.associate { it.first to it.second.map { x -> x.data } }.run {
            this[maxOf { it.key }.apply {
                if (skipArgs)
                    args.increasePointer(this)
            }]!!
        }
    }


    private fun inspect(args: Arguments, lst: MutableList<Pair<Int, List<PriorityReference<T>>>>, depth: Int) {
        for ((x, y) in map) {
            val iter = args.iterator()
            if (x.isMatched(args.increasePointer(true, 1), iter)) {
                if (x.isLastCountMatched(args.increasePointer(true, 1), args.size() - depth)) {
                    lst.add(depth to y.boundedCommand)
                } else {
                    // Next depth..
                    y.inspect(args.increasePointer(true, 1), lst, depth + 1)
                }
            }
        }
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
        (condition ?: (args[pointer] to CommandStorage<T>(args[pointer])).apply {
            map += this
        }.second).registerCommand(commandStart, command, pointer + 1, *args)
    }

    fun getCommand(vararg args: ICommandCondition) = getCommand(*args, pointer = 0)

    fun registerCommand(command: PriorityReference<T>, commandStart: String, vararg args: ICommandCondition) {
        registerCommand(commandStart, command, 0, *args)
    }

    fun inspectNextParameter(prevParam: List<PriorityReference<T>>, arguments: Arguments): List<String> {
        for ((x, y) in map) {
            val iterator = arguments.iterator()
            try {
                if (x.isMatched(arguments, iterator)) {
                    arguments.increasePointer(iterator.forwardedSize())
                    return y.inspectNextParameter(boundedCommand, arguments)
                }
            } catch (_: Exception) {
                // Ignored
            }
        }
        val next = mutableListOf<AutoComplete>()
        for (x in prevParam) {
            x.data.findAnnotations(AutoComplete::class.java)?.forEach {
                next.add(it as AutoComplete)
            }
        }
        val nextParam = mutableListOf<String>()
        if (next.isNotEmpty()) {
            // Use first autocomplete
            // TODO make custom autocomplete
            return nextParam
        }
        // If no auto complete, just return string params
        for ((x, _) in map) {
            if (x is FixedStringCondition)
                nextParam += x.text
        }

        return try {
            val prefix = arguments.next()
            nextParam.filter {
                prefix.isEmpty() || it.startsWith(prefix)
            }
        } catch (e: Exception) {
            nextParam
        }

    }

}