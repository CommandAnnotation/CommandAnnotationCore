package skywolf46.commandannotation.v4.api.abstraction

interface IAnnotationReducer<X : Any> {
    fun reduce(first: X, second: X): Any
}