package skywolf46.commandannotation.kotlin.abstraction

import skywolf46.commandannotation.kotlin.data.Arguments

interface ICommandStorage {
    fun acceptStorage(argument: Arguments) : ICommandStorage
}