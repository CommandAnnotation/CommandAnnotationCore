package skywolf46.commandannotation.v4.api.util

class PeekingIterator<T : Any>(private val arr: Array<T>, private var index: Int = 0) : Iterator<T> {
    override fun hasNext(): Boolean {
        return arr.size > index
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

    fun clone(): PeekingIterator<T> {
        return PeekingIterator(arr, index)
    }

    fun transferTo(iterator: PeekingIterator<T>) {
        iterator.index = index
    }

    fun position(): Int {
        return index
    }
}