package skywolf46.commandannotation.v4.api.util

import skywolf46.commandannotation.v4.api.data.Arguments
import kotlin.reflect.KClass

private val deserializer = mutableMapOf<KClass<*>, (Arguments) -> Any>()

fun <T : Any> Class<T>.registerSerializer(unit: (Arguments) -> T) {
    deserializer[kotlin] = unit
}

fun <T : Any> KClass<T>.registerSerializer(unit: (Arguments) -> T) {
    deserializer[this] = unit
}

