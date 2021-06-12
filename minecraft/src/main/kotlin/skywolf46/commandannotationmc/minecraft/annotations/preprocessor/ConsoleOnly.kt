package skywolf46.commandannotationmc.minecraft.annotations.preprocessor


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ConsoleOnly(vararg val value: String = [""])