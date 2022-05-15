package skywolf46.commandannotation.v4.test.data

import skywolf46.commandannotation.v4.api.abstraction.ICommandInfo

class TestCommandInfo(private val commands: List<String>) : ICommandInfo{
    override fun getCommand(): List<String> {
        return commands
    }
}