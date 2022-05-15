package skywolf46.commandannotation.v4.data

import skywolf46.commandannotation.v4.api.abstraction.ICommand
import skywolf46.commandannotation.v4.api.abstraction.ICommandMatcher
import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.commandannotation.v4.api.util.PeekingIterator
import skywolf46.commandannotation.v4.initializer.CommandCore
import skywolf46.commandannotation.v4.initializer.CommandGeneratorCore

class CommandStorage {
    private val commands = mutableMapOf<ICommandMatcher, CommandStorage>()
    private var currentCommand: ICommand? = null

    fun register(instance: ICommand, command: PeekingIterator<String>) {
        if (!command.hasNext()) {
            currentCommand = instance
            return
        }
        val matcher = CommandCore.findMatcher(command)
            ?: throw IllegalStateException("Impossible error occurred; Null command matcher found from command")
        commands.getOrPut(matcher) {
            CommandStorage()
        }.register(instance, command)
    }

    private fun find(
        args: Arguments,
        iterator: PeekingIterator<String>,
        remapper: MutableList<ICommandMatcher>
    ): ICommand? {
        println("Iterator: ${iterator}")
        if (!iterator.hasNext()) {
            println("No args! Current command: ${currentCommand}")
            // End of cursor : Remapping.
            val baseIterator = args.iterator()
            for (x in remapper) {
                args.appendArgument(x.remap(args, baseIterator)!!)
            }
            args.position(baseIterator.position())
            return currentCommand
        }
        println("Storage: $commands")
        println("Checking for arg ${iterator.peek()}")
        for ((key, storage) in commands) {
            println("Args: $key")
            val iteratorList = remapper.toMutableList()
            val baseIterator = iterator.clone()
            key.remap(args, baseIterator)?.apply {
                iteratorList += key
                println("..To next arg (Next: ${baseIterator.hasNext()})")
                val command = storage.find(args, baseIterator, iteratorList)
                if (command != null) {
                    println("Matched for $key")
                    return command
                }
            } ?: kotlin.run {
                println("Not matched for $key")
            }
        }
        return null
    }

    fun find(args: Arguments) : ICommand? {
        return find(args, args.iterator(), mutableListOf())
    }
}