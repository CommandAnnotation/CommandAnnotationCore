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
        println(iterator.hasNext())
        if (!iterator.hasNext()) {
            // End of cursor : Remapping.
            val baseIterator = args.iterator()
            for (x in remapper) {
                args.appendArgument(x.remap(args, baseIterator)!!)
            }
            args.position(baseIterator.position())
            return currentCommand
        }
        for ((key, storage) in commands) {
            val iteratorList = remapper.toMutableList()
            val baseIterator = iterator.clone()
            key.remap(args, baseIterator)?.apply {
                iteratorList += key
                val command = storage.find(args)
                if (command != null) {
                    return command
                }
            }
        }
        return null
    }

    fun find(args: Arguments) : ICommand? {
        return find(args, args.iterator(), mutableListOf())
    }
}