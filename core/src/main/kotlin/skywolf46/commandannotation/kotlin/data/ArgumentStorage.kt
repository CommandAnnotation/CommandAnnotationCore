package skywolf46.commandannotation.kotlin.data

import skywolf46.commandannotation.kotlin.util.ClassUtil
import kotlin.reflect.KClass

class ArgumentStorage {
    private val arguments = mutableMapOf<Class<Any>, MutableList<Any>>()
    private val argumentFixed = mutableMapOf<String, Any>()

    operator fun get(str: String) = argumentFixed[str]
    operator fun get(cls: Class<*>) = arguments[cls]
    operator fun get(cls: KClass<*>) = arguments[cls.java]


    fun setArgument(key: String, any: Any) {
        argumentFixed[key] = any
    }

    fun addArgument(any: Any) {
        ClassUtil.iterateParent(any.javaClass) {
            arguments.computeIfAbsent(this) { mutableListOf() }.add(any)
        }
    }

}