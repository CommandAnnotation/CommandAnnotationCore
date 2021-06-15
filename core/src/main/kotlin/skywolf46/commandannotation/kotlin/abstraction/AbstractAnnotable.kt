package skywolf46.commandannotation.kotlin.abstraction

import skywolf46.commandannotation.kotlin.CommandAnnotationCore
import skywolf46.commandannotation.kotlin.data.Arguments
import skywolf46.extrautility.util.MethodInvoker
import java.lang.reflect.Proxy
import kotlin.reflect.jvm.kotlinFunction

abstract class AbstractAnnotable(val wrapper: MethodInvoker, val priority: Int = 0) : Comparable<AbstractAnnotable> {
    val markedMap = mutableMapOf<Class<*>, Annotation>()
    val annotationPriority = mutableListOf<Annotation>()

    init {
        for (x in wrapper.method.kotlinFunction!!.annotations) {
            var target = x
            var targetCls: Class<*> = x::class.java

            if (Proxy.isProxyClass(targetCls)) {
                targetCls = targetCls.interfaces[0]
            }
            if (CommandAnnotationCore.preprocessors[targetCls as Class<Annotation>] != null) {
                markedMap[x::class.java] = x as Annotation
                annotationPriority += x as Annotation
            }
        }

        annotationPriority.sortWith { first, second ->
            CommandAnnotationCore.preprocessors[first::class.java.findAnnotationClass() as Class<Annotation>]!!.priority.compareTo(
                CommandAnnotationCore.preprocessors[second::class.java.findAnnotationClass() as Class<Annotation>]!!.priority)
        }
    }

    fun findMarked(target: Class<out Annotation>) = markedMap[target]

    fun runMarked(storage: Arguments): Boolean {
        for (x in annotationPriority) {
            if (!CommandAnnotationCore.preprocessors.invoke(storage, x)) {
                return false
            }
        }
        val result = wrapper.call(storage._storage)
        if (result is Boolean && !result)
            return false

        return true
    }

    override fun compareTo(other: AbstractAnnotable): Int {
        return priority.compareTo(other.priority)
    }

    fun Class<out Annotation>.findAnnotationClass() : Class<out Annotation>{
        if(Proxy.isProxyClass(this))
            return this.interfaces[0] as Class<Annotation>
        return this
    }
}