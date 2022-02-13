package skywolf46.commandannotation.v4.api.abstraction

interface IAnnotationConverter<T : Annotation> {
    fun convertTo(annotation: T): Any
}