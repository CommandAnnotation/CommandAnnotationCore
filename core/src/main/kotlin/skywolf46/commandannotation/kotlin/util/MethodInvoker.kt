package skywolf46.commandannotation.kotlin.util

import skywolf46.commandannotation.kotlin.annotation.parameter.Named
import skywolf46.commandannotation.kotlin.data.ArgumentStorage
import java.lang.reflect.Method

class MethodInvoker(val method: Method, private val instance: Any?) {
    private val indexed = mutableListOf<Class<*>?>()
    private val variableIndexed = mutableMapOf<Int, Pair<String, Class<*>>>()

    init {
        for (i in 0 until method.parameterCount) {
            val param = method.parameters[i]
            val annot = param.getAnnotation(Named::class.java)
            if (annot != null) {
                variableIndexed[i] = annot.name to param.type
                indexed.add(null)
            } else {
                indexed.add(param.type)
            }
        }
    }

    operator fun invoke(storage: ArgumentStorage) {
        val arrNullable = Array<Any?>(indexed.size) { null }
        for (x in 0 until indexed.size) {
            arrNullable[x] = variableIndexed[x]?.run { storage[this.first] } ?: storage[indexed[x]!!]
        }
        method.invoke(instance, *arrNullable)
    }
}