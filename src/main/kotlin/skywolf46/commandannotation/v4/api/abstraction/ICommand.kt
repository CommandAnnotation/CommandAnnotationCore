package skywolf46.commandannotation.v4.api.abstraction

import skywolf46.commandannotation.v4.api.data.Arguments

interface ICommand {
    fun invokeCommand(arguments: Arguments)

    fun getPriority(): Int
}