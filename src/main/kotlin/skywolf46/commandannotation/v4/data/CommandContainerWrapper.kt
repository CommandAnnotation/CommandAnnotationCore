package skywolf46.commandannotation.v4.data

import skywolf46.extrautility.data.ArgumentStorage

class CommandContainerWrapper(command: List<String>) {
    val command = command.distinct()
}