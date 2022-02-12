package skywolf46.commandannotation.v4.api.annotations

import skywolf46.commandannotation.v4.constants.AnnotationConstant

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)
annotation class InjectValue(val fieldName: String = AnnotationConstant.EMPTY_VALUE)