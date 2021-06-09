package skywolf46.commandannotation.kotlin.data

import skywolf46.extrautility.data.ArgumentStorage

class MarkedStorage {

    private val markedList = ArgumentStorage()

    fun <X : Annotation> getMarked(cls: Class<X>) = markedList[cls]

    fun addMarked(any: Any) {
        markedList.addArgument(any)
    }
}