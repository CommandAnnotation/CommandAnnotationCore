package skywolf46.commandannotation.v4.api.util

import skywolf46.commandannotation.v4.api.data.Arguments
import kotlin.reflect.KClass

private val deserializer = mutableMapOf<KClass<*>, (Arguments) -> Any?>()

fun <T : Any> Class<T>.registerDeserializer(unit: (Arguments) -> T) {
    deserializer[kotlin] = unit
}

fun <T : Any> KClass<T>.registerDeserializer(unit: (Arguments) -> T) {
    deserializer[this] = unit
}


fun <T : Any> KClass<T>.deserialize(args: Arguments): Any? {
    try {
        return (deserializer[this] ?: throw IllegalStateException("No deserializer found for class $qualifiedName"))
            .invoke(args)
    } catch (e: Throwable) {
        e.printStackTrace()
        throw e
    }
}