package skywolf46.commandannotation.v4.util

inline fun <T : Any> T.just(unit: () -> Unit): T {
    unit()
    return this
}