package skywolf46.commandannotation.kotlin.data

import skywolf46.commandannotation.kotlin.abstraction.ICommand
import skywolf46.extrautility.data.ArgumentStorage
import skywolf46.extrautility.util.PriorityReference

class CommandWrapper(val command: ICommand) {
    private val preProcessor = object : ArrayList<PriorityReference<ArgumentStorage.() -> Boolean>>() {
        override fun add(element: PriorityReference<ArgumentStorage.() -> Boolean>): Boolean {
            val temp = super.add(element)
            sort()
            return temp
        }
    }

    fun addPreprocessor(ref: PriorityReference<ArgumentStorage.() -> Boolean>) {
        preProcessor += ref
    }

    fun invoke(storage: ArgumentStorage) {
        for (x in preProcessor) {
            if (!x.data(storage))
                return
        }
        command.invoke(storage)
    }
}