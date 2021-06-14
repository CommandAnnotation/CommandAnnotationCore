package skywolf46.commandannotation.kotlin.data

import skywolf46.commandannotation.kotlin.annotation.Force
import skywolf46.commandannotation.kotlin.annotation.Mark
import skywolf46.extrautility.data.ArgumentStorage

class MarkedStorage {

    private val markedList = ArgumentStorage()

    val forcedMarks = mutableListOf<MarkedMethod>()

    fun <X : Annotation> getMarked(cls: Class<X>) = markedList[cls] as List<Annotation>


    fun getMarked(name: String): MarkedMethod? = markedList[name] as MarkedMethod?

    fun addMarked(mark: MarkedMethod) {
        for ((x, y) in mark.markedMap) {
            val annotation = mark.wrapper.method.getDeclaredAnnotation(Mark::class.java)
            markedList.add(x, mark)
            markedList.setArgument(annotation.value, mark)
        }
    }
}