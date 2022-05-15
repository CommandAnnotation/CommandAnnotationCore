package skywolf46.commandannotation.v4.data

import skywolf46.commandannotation.v4.api.abstraction.ICommandInfo
import skywolf46.extrautility.data.ArgumentStorage

class CommandContainerWrapper(command: List<String>) : ICommandInfo {
    private val commands = command.distinct()

    override fun getCommand(): List<String> {
        return commands
    }
}