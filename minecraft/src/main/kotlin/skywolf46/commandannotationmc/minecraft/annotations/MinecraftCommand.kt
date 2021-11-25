package skywolf46.commandannotationmc.minecraft.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class MinecraftCommand(
    vararg val value: String,
    val priority: Int = 0,
    val requireParameters: Boolean = false,
    val bridging: Boolean = false,
) {

}