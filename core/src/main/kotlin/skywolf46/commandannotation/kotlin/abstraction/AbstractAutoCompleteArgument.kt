package skywolf46.commandannotation.kotlin.abstraction

import skywolf46.extrautility.data.ArgumentStorage

abstract class AbstractAutoCompleteArgument {
    protected abstract fun filter(storage: ArgumentStorage, str: String): MutableList<String>

    fun filterArguments(storage: ArgumentStorage, str: String): List<String> {
        val lowerCased = str.toLowerCase()
        return filter(storage, str).apply {
            removeIf { current -> str.isNotEmpty() && !current.toLowerCase().startsWith(lowerCased) }
        }
    }
}