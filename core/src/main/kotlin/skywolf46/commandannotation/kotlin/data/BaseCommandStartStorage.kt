package skywolf46.commandannotation.kotlin.data

import skywolf46.commandannotation.kotlin.abstraction.ICommand
import skywolf46.commandannotation.kotlin.abstraction.ICommandCondition

class BaseCommandStartStorage<T : ICommand> {
    private val map = mutableMapOf<String, CommandStorage<T>>()

    fun register(command: T, commandStart: String, vararg inspected: ICommandCondition) {
        if (!map.containsKey(commandStart))
            map[commandStart] = CommandStorage()
        map[commandStart]?.registerCommand(command, commandStart, *inspected)
    }

    fun inspect(commandStart: String, args: Arguments): T? {
        return map[commandStart]?.inspectCommand(args)
    }


    fun get(commandStart: String, vararg args: ICommandCondition): T? {
        return map[commandStart]?.getCommand(*args)
    }
}