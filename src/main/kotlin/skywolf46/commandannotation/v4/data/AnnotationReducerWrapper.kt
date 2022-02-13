package skywolf46.commandannotation.v4.data

class AnnotationReducerWrapper<X : Any>(val reducer: X.(X) -> X) {
    fun reduce(first: X, second: X): X {
        return reducer(first, second)
    }
}