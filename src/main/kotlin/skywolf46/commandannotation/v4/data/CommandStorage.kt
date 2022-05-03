package skywolf46.commandannotation.v4.data

import skywolf46.commandannotation.v4.api.abstraction.ICommand
import skywolf46.commandannotation.v4.api.abstraction.ICommandMatcher

class CommandStorage {
    private val commands = mutableMapOf<ICommandMatcher, CommandStorage>()
    private var currentCommand : ICommand? = null
}