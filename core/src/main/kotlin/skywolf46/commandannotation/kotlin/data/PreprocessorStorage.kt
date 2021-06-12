package skywolf46.commandannotation.kotlin.data

import skywolf46.extrautility.util.PriorityReference
import java.lang.reflect.Proxy

class PreprocessorStorage {
    val processor = mutableMapOf<Class<Annotation>, PriorityReference<Arguments.(Any) -> Boolean>>()
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
        var cls: Class<*> = annotation::class.java
        if (Proxy.isProxyClass(cls)) {
            cls = cls.interfaces[0]
        }
        processor[cls]?.apply {
            if (!data(arguments, annotation))
                return false
        }
        return true
    }

    fun getKeys() = processor.keys
}