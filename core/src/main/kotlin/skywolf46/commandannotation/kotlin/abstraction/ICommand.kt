package skywolf46.commandannotation.kotlin.abstraction

import skywolf46.extrautility.data.ArgumentStorage

interface ICommand {
    fun getCommand(): StringArray

    fun invoke(storage: ArgumentStorage)

    fun bindUnregisterLambda(unit: () -> Unit)
}