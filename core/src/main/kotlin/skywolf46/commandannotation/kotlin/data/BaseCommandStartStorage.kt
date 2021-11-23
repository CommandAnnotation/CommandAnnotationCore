package skywolf46.commandannotation.kotlin.data

import skywolf46.commandannotation.kotlin.abstraction.ICommandCondition
import skywolf46.commandannotation.kotlin.impl.AbstractCommand
import skywolf46.extrautility.util.PriorityReference

class BaseCommandStartStorage<T : AbstractCommand> {
    private val map = mutableMapOf<String, CommandStorage<T>>()
    fun register(command: PriorityReference<T>, commandStart: String, vararg inspected: ICommandCondition) {
        if (!map.containsKey(commandStart))
            map[commandStart] = CommandStorage()
        map[commandStart]?.registerCommand(command, commandStart, *inspected)
    }

    fun inspectNextParameter(commandStart: String, args: Arguments): List<String> {
        if (!map.containsKey(commandStart))
            return emptyList()
        return map[commandStart]!!.inspectNextParameter(emptyList(), args)
    }

    fun inspectNextCondition(commandStart: String, args: Arguments): List<ICommandCondition> {
        if (!map.containsKey(commandStart))
            return emptyList()
        val next = map[commandStart]!!.inspectNextCondition(args, 0)
        // ..Starting from depth level 1..
        if(next.first + 1 != args.fullSize())
            return emptyList()
        return next.second
    }

    fun inspect(commandStart: String, args: Arguments): List<T> {
        return map[commandStart]?.inspect(args, true) ?: emptyList()
    }


    fun get(commandStart: String, vararg args: ICommandCondition): List<T> {
        return map[commandStart]?.getCommand(*args) ?: emptyList()
    }


}