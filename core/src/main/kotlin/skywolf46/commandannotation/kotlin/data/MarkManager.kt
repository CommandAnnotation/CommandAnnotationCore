package skywolf46.commandannotation.kotlin.data

import skywolf46.commandannotation.kotlin.abstraction.ICommand
import skywolf46.commandannotation.kotlin.annotation.Apply
import skywolf46.commandannotation.kotlin.annotation.Force
import skywolf46.commandannotation.kotlin.annotation.Mark
import skywolf46.commandannotation.kotlin.enums.VisibilityScope
import skywolf46.extrautility.util.MethodInvoker
import java.lang.reflect.Method

class MarkManager {
    private val markedGlobal = MarkedStorage()
    private val markedProject = mutableMapOf<String, MarkedStorage>()
    private val markedClass = mutableMapOf<Class<*>, MarkedStorage>()
    private val cachedAnnotations = mutableMapOf<Method, Map<Class<out Annotation>, List<Annotation>>>()
    private val cachedMarks = mutableMapOf<Method, MutableList<MarkedMethod>>()
    fun registerMark(invoker: MethodInvoker) {
        invoker.method.getDeclaredAnnotation(Mark::class.java)?.apply {
            val data: MarkedStorage = when (this.scope) {
                VisibilityScope.GLOBAL -> markedGlobal
                VisibilityScope.PROJECT -> markedProject.computeIfAbsent(invoker.method.declaringClass.toProject()) { MarkedStorage() }
                VisibilityScope.CLASS -> markedClass.computeIfAbsent(invoker.method.declaringClass) { MarkedStorage() }
            }
            val marked = MarkedMethod(invoker, this.force)
            val isForced = invoker.method.getDeclaredAnnotation(Force::class.java) != null
            println("CommandAnnotation-Core | ..Registered Mark \"${this.value}\"(Scope $scope${if (isForced) ", Forced" else ""}) at ${invoker.method.declaringClass.kotlin.qualifiedName}#${invoker.method.name}")
            data.addMarked(marked)
            if (isForced) {
                data.forcedMarks.add(marked)
            }
        }
    }

    fun findMarkedMethods(baseCommand: ICommand): List<MarkedMethod> {
        return findMarked(baseCommand).second
    }

    fun findMarkedAnnotations(baseCommand: ICommand, target: Class<Annotation>): List<Annotation> {
        return findMarked(baseCommand).first[target] ?: listOf()
    }

    fun findMarked(baseCommand: ICommand): Pair<Map<Class<out Annotation>, List<Annotation>>, List<MarkedMethod>> {
        var foundList = cachedMarks[baseCommand.getMethod().method]
        return foundList?.run {
            return@run cachedAnnotations[baseCommand.getMethod().method]!! to foundList!!
        } ?: run {
            foundList = mutableListOf()
            baseCommand.getMethod().method.getAnnotation(Apply::class.java)?.apply {
                for (x in value) {
                    findMarked(x, baseCommand.getMethod().method)?.apply {
                        foundList!!.add(this)
                    }
                }
            }
            for (x in markedGlobal.forcedMarks)
                foundList!! += x
            markedProject[baseCommand.getMethod().method.declaringClass.toProject()]?.apply {
                for (x in forcedMarks)
                    foundList!! += x
            }

            markedClass[baseCommand.getMethod().method.declaringClass]?.apply {
                for (x in forcedMarks)
                    foundList!! += x
            }
            cachedMarks[baseCommand.getMethod().method] = foundList!!
            // Cache only compatible annotations
            return@run (cachedAnnotations.computeIfAbsent(baseCommand.getMethod().method) {
                val map = mutableMapOf<Class<out Annotation>, MutableList<Annotation>>()
                for (x in foundList as MutableList<MarkedMethod>) {
                    for ((key, annot) in x.markedMap) {
                        map.computeIfAbsent(key as Class<out Annotation>) { mutableListOf() }.add(annot)
                    }
                }
                return@computeIfAbsent map
            }) to foundList!!
        }
    }

    fun findMarked(markName: String, scopingEntity: Method): MarkedMethod? {
        // Finding priority: Class -> Project -> Priority
        return markedClass[scopingEntity.declaringClass]
            ?.getMarked(markName)
            ?: markedProject[scopingEntity.declaringClass.toProject()]
                ?.getMarked(markName)
            ?: markedGlobal.getMarked(markName)
    }

    private fun Class<*>.toProject(): String {
        val name = name.split(".")
        return when (name.size) {
            0 -> ""
            1 -> name[0]
            else -> "$name[0].$name[1]"
        }
    }
}