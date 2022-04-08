package skywolf46.commandannotation.v4.util

import skywolf46.commandannotation.v4.api.annotations.debug.AddonDevelopmentMethod


@Deprecated("Will move to SkywolfExtraUtility")
object ClassUtilTemp {
    // Find intersection of two class.
    fun findClassIntersection(clsFirst: Class<*>, clsSecond: Class<*>): List<Class<*>> {
        val classListFirst = collectClasses(clsFirst)
        val classListSecond = collectClasses(clsSecond)
        val intersection = mutableListOf<Class<*>>()
        classListFirst.forEach {
            if (classListSecond.contains(it)) {
                intersection.add(it)
            }
        }
        return intersection.asReversed()
    }

    @AddonDevelopmentMethod
    fun collectClasses(cls: Class<*>): MutableList<Class<*>> {
        val clsList = mutableListOf<Class<*>>()
        var clsCurrent = cls
        do {
            clsList.add(clsCurrent)
            for (x in clsCurrent.interfaces) {
                clsList.add(x)
            }
            clsCurrent = clsCurrent.superclass
        } while (clsCurrent != Any::class.java)
        return clsList.distinct() as MutableList<Class<*>>
    }
}