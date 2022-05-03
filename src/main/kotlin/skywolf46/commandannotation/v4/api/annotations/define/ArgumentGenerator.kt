package skywolf46.commandannotation.v4.api.annotations.define

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ArgumentGenerator(val bindAt: Array<KClass<out Annotation>>)