package skywolf46.commandannotation.v4.data

import skywolf46.extrautility.util.MethodWrapper
import java.lang.reflect.Method
import java.util.function.Function

class AnnotationConverterWrapper<X : Annotation>(val handler: (X) -> Any) {
    companion object {
        fun <X : Annotation> fromMethod(method: MethodWrapper): AnnotationConverterWrapper<X> {
            return AnnotationConverterWrapper { annotation -> method.invoke(annotation)!! }
        }

        fun fromMethod(method: Method) =
            fromMethod<Annotation>(MethodWrapper(method, null))

    }

    constructor(handler: Function<Annotation, Any>) : this({ args -> handler.apply(args) })

    fun convert(annotation: X): Any {
        return handler(annotation)
    }

}