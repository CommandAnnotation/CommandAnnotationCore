package skywolf46.commandannotation.v4.data

import skywolf46.commandannotation.v4.api.abstraction.ICommand
import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.commandannotation.v4.api.util.PeekingIterator
import skywolf46.extrautility.data.ArgumentStorage

class CommandBaseStorage {
    private val storages = mutableMapOf<String, CommandStorage>()

    fun find(storage: Arguments): ICommand? {
        // Pointer will start from 1
        val baseCommand = storage.arg()
        return storages[baseCommand]?.find(storage)
    }

    fun register(instance: ICommand, iterator: PeekingIterator<String>) {
        val baseCommand = iterator.next()
        storages.getOrPut(baseCommand) { CommandStorage() }.register(instance, iterator)
    }
}