package skywolf46.commandannotation.kotlin.data

import skywolf46.extrautility.data.ArgumentStorage
import skywolf46.extrautility.util.PriorityReference

class PreprocessorStorage {
    private val processor = mutableMapOf<Class<*>, PriorityReference<ArgumentStorage.() -> Boolean>>()

    fun register(annotation: Class<Annotation>, priority: Int, unit: ArgumentStorage.() -> Boolean) {
        processor[annotation] = PriorityReference(unit, priority)
    }

    operator fun get(cls: Class<Annotation>): PriorityReference<ArgumentStorage.() -> Boolean>? = processor[cls]

    fun getKeys() = processor.keys
}