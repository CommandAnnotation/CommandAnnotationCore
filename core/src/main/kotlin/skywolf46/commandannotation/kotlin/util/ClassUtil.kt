package skywolf46.commandannotation.kotlin.util

object ClassUtil {
    inline fun iterateParent(cls: Class<Any>, iterator: Class<Any>.() -> Unit) {
        var clsOrig = cls
        do {
            iterator(clsOrig)
            clsOrig = clsOrig.superclass
        } while (cls != Any::class.java)
        iterator(Any::class.java)
    }
}