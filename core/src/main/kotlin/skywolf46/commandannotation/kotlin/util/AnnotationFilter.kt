package skywolf46.commandannotation.kotlin.util

import kotlin.reflect.KClass

object AnnotationFilter {
    fun filter(cls: Class<Any>, annot: KClass<Any>): List<MethodInvoker> {
        val lst = mutableListOf<MethodInvoker>()

        return lst
    }
}