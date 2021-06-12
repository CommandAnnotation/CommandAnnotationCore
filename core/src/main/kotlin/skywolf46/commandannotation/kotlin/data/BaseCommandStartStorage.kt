package skywolf46.commandannotation.kotlin.data

import skywolf46.commandannotation.kotlin.abstraction.ICommand
import skywolf46.commandannotation.kotlin.abstraction.ICommandCondition
import skywolf46.extrautility.util.PriorityReference

class BaseCommandStartStorage<T : ICommand> {
    private val map = mutableMapOf<String, CommandStorage<T>>()
    fun register(command: PriorityReference<T>, commandStart: String, vararg inspected: ICommandCondition) {
        if (!map.containsKey(commandStart))
            map[commandStart] = CommandStorage()
        map[commandStart]?.registerCommand(command, commandStart, *inspected)
    }

    fun inspect(commandStart: String, args: Arguments): List<T> {
        return map[commandStart]?.inspectCommand(args) ?: emptyList()
    }


    fun get(commandStart: String, vararg args: ICommandCondition): List<T> {
        return map[commandStart]?.getCommand(*args) ?: emptyList()
    }

}