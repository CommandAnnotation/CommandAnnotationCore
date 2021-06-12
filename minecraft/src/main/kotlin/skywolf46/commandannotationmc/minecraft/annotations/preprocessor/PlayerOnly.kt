package skywolf46.commandannotationmc.minecraft.annotations.preprocessor


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class PlayerOnly(vararg val value: String = [""])