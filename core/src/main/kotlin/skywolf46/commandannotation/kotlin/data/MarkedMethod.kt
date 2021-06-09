package skywolf46.commandannotation.kotlin.data

import skywolf46.extrautility.data.ArgumentStorage

class MarkedMethod {
    val marked = mutableMapOf<Class<*>, Pair<Annotation, Any>>()

    fun <X : Annotation, V : Any> findMarked(target: Class<X>) = marked[target]

    fun registerMarked() {

    }
}