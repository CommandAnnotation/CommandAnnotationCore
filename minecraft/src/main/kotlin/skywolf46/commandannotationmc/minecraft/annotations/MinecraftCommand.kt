package skywolf46.commandannotationmc.minecraft.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class MinecraftCommand(
    vararg val values: String,
    val requireParameters: Boolean = false,
    val bridging: Boolean = false,
) {

}