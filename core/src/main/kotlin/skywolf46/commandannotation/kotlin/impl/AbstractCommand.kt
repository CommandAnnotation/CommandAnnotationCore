package skywolf46.commandannotation.kotlin.impl

import skywolf46.commandannotation.kotlin.CommandAnnotationCore
import skywolf46.commandannotation.kotlin.abstraction.AbstractAnnotable
import skywolf46.commandannotation.kotlin.abstraction.ICommand
import skywolf46.commandannotation.kotlin.abstraction.ICommandCondition
import skywolf46.commandannotation.kotlin.data.Arguments
import skywolf46.commandannotation.kotlin.util.CommandInspector
import skywolf46.extrautility.util.MethodInvoker

abstract class AbstractCommand(
    protected val command: Array<out String>,
    condition: Array<out ICommandCondition>,
    wrapper: MethodInvoker,
    priority: Int = 0,
) :
    AbstractAnnotable(wrapper, priority), ICommand {

    companion object {
        private val completeMap = mutableMapOf<String, Int>()
    }


    constructor(commandStart: String, wrapper: MethodInvoker) : this(arrayOf(commandStart),
        emptyArray<ICommandCondition>(),
        wrapper)

    private var inspectedSize = 0

    init {
        if (command.size == 1) {
            inspectedSize = condition.size
        }
    }

    override fun getRawCommand(): Array<out String> {
        return command
    }

    override fun invoke(storage: Arguments): Boolean {
        storage._sysPointer = inspectedSize
        CommandAnnotationCore.markManager.findMarkedMethods(this).forEach { methods ->
            if (!methods.runMarked(storage)) {
                return true
            }
        }

        for (x in annotationPriority) {
            if (!CommandAnnotationCore.preprocessors.invoke(storage, x)) {
                return false
            }
        }

        val result = wrapper.invoke(storage._storage)
        if (result is Boolean && result)
            return result
        return true
    }

    override fun onUnregister(commandStart: String, vararg condition: ICommandCondition) {
        completeMap[commandStart]?.apply {
            if (this <= 1) {
                completeMap.remove(commandStart)
                onBaseCommandRegister(commandStart, *condition)
            } else {
                completeMap[commandStart] = (this - 1)
            }
        }
        onCommandUnregister(commandStart, *condition)
    }

    override fun onRegister(commandStart: String, vararg condition: ICommandCondition) {
        if (!completeMap.containsKey(commandStart)) {
            onBaseCommandRegister(commandStart, *condition)
        }
        completeMap[commandStart] = (completeMap[commandStart] ?: 0) + 1
        onCommandRegister(commandStart, *condition)
    }


    protected abstract fun onBaseCommandRegister(commandStart: String, vararg condition: ICommandCondition)

    protected abstract fun onCommandRegister(commandStart: String, vararg condition: ICommandCondition)

    protected abstract fun onBaseCommandUnregister(commandStart: String, vararg condition: ICommandCondition)

    protected abstract fun onCommandUnregister(commandStart: String, vararg condition: ICommandCondition)

    fun findAnnotations(annot: Class<out Annotation>): List<Annotation> {
        val lst = mutableListOf<Annotation>()
        findMarked(annot)?.apply {
            lst += this
        }
        lst += CommandAnnotationCore.markManager.findMarkedAnnotations(this, annot as Class<Annotation>)
        return lst
    }


}