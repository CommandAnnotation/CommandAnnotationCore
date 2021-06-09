package skywolf46.commandannotation.kotlin.data

import skywolf46.extrautility.data.ArgumentStorage
import skywolf46.extrautility.util.PriorityReference

class PreprocessorStorage {
    private val processor = mutableMapOf<Class<Annotation>, PriorityReference<Arguments.(Any) -> Boolean>>()
    private val processorList = object : ArrayList<PriorityReference<Arguments.(Any) -> Boolean>>() {
        override fun add(element: PriorityReference<Arguments.(Any) -> Boolean>): Boolean {
            val added = super.add(element)
            sort()
            return added
        }
    }

    fun <X : Annotation> register(annotation: Class<X>, priority: Int, unit: Arguments.(X) -> Boolean) {
        (PriorityReference(unit, priority) as PriorityReference<Arguments.(Any) -> Boolean>).apply {
            processor[annotation as Class<Annotation>] = this
            processorList += this
        }
    }

    operator fun get(cls: Class<Annotation>): PriorityReference<Arguments.(Any) -> Boolean>? = processor[cls]

    operator fun invoke(arguments: Arguments, annotation: Annotation): Boolean {
        for (x in processorList)
            if (!x.data(arguments, annotation))
                return false
        return true
    }

    fun getKeys() = processor.keys
}