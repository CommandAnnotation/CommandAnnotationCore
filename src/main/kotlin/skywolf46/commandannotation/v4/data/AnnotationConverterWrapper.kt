package skywolf46.commandannotation.v4.data

import skywolf46.extrautility.core.util.ReflectionUtil
import skywolf46.extrautility.core.util.asSingletonCallable
import java.lang.reflect.Method
import java.util.function.Function

class AnnotationConverterWrapper<X : Annotation>(val handler: (X) -> Any) {
    companion object {
        fun <X : Annotation> fromMethod(method: ReflectionUtil.CallableFunction): AnnotationConverterWrapper<X> {
            return AnnotationConverterWrapper { annotation -> method.invoke(mutableListOf(annotation))!! }
        }

        fun fromMethod(method: Method) =
            fromMethod<Annotation>(method.asSingletonCallable())

    }

    constructor(handler: Function<Annotation, Any>) : this({ args -> handler.apply(args) })

    fun convert(annotation: X): Any {
        return handler(annotation)
    }

}