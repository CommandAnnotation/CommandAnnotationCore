package skywolf46.commandannotation.v4.data

import skywolf46.commandannotation.v4.api.abstraction.ICommand
import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.commandannotation.v4.api.util.PeekingIterator
import skywolf46.commandannotation.v4.constants.CommandMatcherWrapper
import skywolf46.commandannotation.v4.initializer.CommandCore

class CommandStorage {
    private val commands = mutableMapOf<CommandMatcherWrapper, CommandStorage>()
    private val sortedCommandMatcher = object : ArrayList<CommandMatcherWrapper>() {
        override fun add(element: CommandMatcherWrapper): Boolean {
            val result = super.add(element)
            sort()
            return result
        }
    }
    private var currentCommand: ICommand? = null

    fun register(instance: ICommand, command: PeekingIterator<String>) {
        if (!command.hasNext()) {
            currentCommand = instance
            return
        }
        val matcher = CommandCore.findMatcher(command)
            ?: throw IllegalStateException("Impossible error occurred; Null command matcher found from command")
        commands.getOrPut(matcher) {
            sortedCommandMatcher += matcher
            CommandStorage()
        }.register(instance, command)
    }

    private fun find(
        args: Arguments,
        iterator: PeekingIterator<String>,
        remapper: MutableList<CommandMatcherWrapper>
    ): ICommand? {
        println("Commands: ${sortedCommandMatcher}")
        if (!iterator.hasNext()) {
            println("No args! Current command: ${currentCommand}")
            // End of cursor : Remapping.
            val baseIterator = args.iterator()
            for (x in remapper) {
                x.remap(args, baseIterator)?.apply {
                    args.appendArgument(this)
                }
            }
            args.position(baseIterator.position())
            return currentCommand
        }
        for (key in sortedCommandMatcher) {
            println("Args: $key")
            val iteratorList = remapper.toMutableList()
            val baseIterator = iterator.clone()
            if (key.isMatched(args, baseIterator)) {
                iteratorList += key
                println("..To next arg (Next: ${baseIterator.hasNext()})")
                val command = commands[key]!!.find(args, baseIterator, iteratorList)
                if (command != null) {
                    println("Matched for $key")
                    return command
                }
            } else {
                println("Not matched for $key")
            }
        }
        // Return fallback if scan failed.
        // Do remap, cause cannot reach to end of cursor.
        val baseIterator = args.iterator()
        for (x in remapper) {
            println("Remapper: $x / ${baseIterator.position()}")

            x.remap(args, baseIterator)?.apply {
                println("Appending: ${this}")
                args.appendArgument(this)
            }
        }
        args.position(baseIterator.position())
        return currentCommand
    }

    fun find(args: Arguments): ICommand? {
        return find(args, args.iterator(), mutableListOf())
    }
}