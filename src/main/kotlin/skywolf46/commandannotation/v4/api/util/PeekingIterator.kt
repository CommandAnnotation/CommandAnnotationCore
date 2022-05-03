package skywolf46.commandannotation.v4.api.util

class PeekingIterator<T : Any>(private val arr: Array<T>) : Iterator<T> {
    private var index = 0
    override fun hasNext(): Boolean {
        return arr.size <= index
    }

    override fun next(): T {
        return arr[index++]
    }

    fun prev(): T {
        if (index == 0)
            throw IndexOutOfBoundsException("Index out of range; Cannot get previous value when pointer is zero")
        return arr[--index]
    }

    fun peek(skip: Int = 0): T {
        return arr[index + skip]
    }

}