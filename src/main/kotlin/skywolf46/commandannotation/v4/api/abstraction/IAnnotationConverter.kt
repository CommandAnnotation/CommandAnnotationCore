package skywolf46.commandannotation.v4.api.abstraction

import skywolf46.commandannotation.v4.api.annotations.define.AnnotationConverter

interface IAnnotationConverter<T : Annotation> {
    @AnnotationConverter
    fun convertTo(annotation: T): Any
}