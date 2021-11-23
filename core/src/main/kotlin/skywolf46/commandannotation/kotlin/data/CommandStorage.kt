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
        return mutableListOf<Pair<Int, List<PriorityReference<T>>>>().apply {
            // Add default.
            add(0 to boundedCommand)
            inspect(args, this, 0)
        }.run {
            println(this)
            if (isEmpty())
                emptyList()
            else {
                sortedByDescending { it.first }.apply {
                    println(this)
                }[0].second
                    .sortedBy { x -> x.priority }
                    .map { x -> x.data }
            }
        }
    }

    // /test <test> etst
    private fun inspect(
        arguments: Arguments,
        list: MutableList<Pair<Int, List<PriorityReference<T>>>>,
        depth: Int,
    ) {
        var isMatched = false
        for ((x, y) in map) {
            // Iterate..
            val iterator = arguments.iterator()
            if (x.isMatched(arguments, iterator)) {
                isMatched = true
                y.inspect(arguments.increasePointer(true, iterator.forwardedSize()), list, depth + 1)
            }
        }
        if (!isMatched)
            list += depth to boundedCommand
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

    fun inspectNextCondition(arguments: Arguments, depth: Int): Pair<Int, List<ICommandCondition>> {
        if (arguments.size() == 1) {
            println("Zero-argument, returning all")
            println(map)
            // If last, return all.
            val nextParam = map.map { (x, _) -> x }
            return depth to nextParam
        }
        for ((x, y) in map) {
            val iterator = arguments.iterator()
            try {
                if (x.isMatched(arguments, iterator)) {
                    println("Matched")
                    arguments.increasePointer(iterator.forwardedSize())
                    return y.inspectNextCondition(arguments, depth + 1)
                }
            } catch (_: Exception) {
                // Ignored
            }
        }
        val nextParam = map.map { (x, _) -> x }
        return depth to nextParam
    }
}