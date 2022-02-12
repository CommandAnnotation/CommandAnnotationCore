package skywolf46.commandannotation.v4.data

import skywolf46.commandannotation.v4.abstraction.AbstractCommandCondition
import skywolf46.commandannotation.v4.abstraction.IConditionMixer
import skywolf46.commandannotation.v4.abstraction.IRequirement
import skywolf46.commandannotation.v4.annotations.debug.RequireSingleThread
import skywolf46.commandannotation.v4.exceptions.CommandFailedException
import java.util.function.Consumer

@RequireSingleThread
class Requirement(val args: Arguments) : IRequirement {
    private val conditions = mutableListOf<AbstractCommandCondition>()
    private val onFail = mutableListOf<(Arguments) -> Unit>()

    override fun addCondition(condition: AbstractCommandCondition): RequirementPrepareProxy {
        conditions.add(condition)
        return RequirementPrepareProxy(this)
    }

    override fun prepareCondition(condition: AbstractCommandCondition): RequirementPrepareProxy {
        return addCondition(condition)
    }


    fun mixCondition(mixer: IConditionMixer) {
        val first = conditions.removeAt(conditions.size - 2)
        val second = conditions.removeAt(conditions.size - 1)
        conditions.add(mixer.mix(first, second))
    }

    fun addFailHandler(handler: (Arguments) -> Unit) {
        onFail += handler
    }

    fun addFailHandler(handler: Consumer<Arguments>) {
        addFailHandler { args -> handler.accept(args) }
    }


    fun onFail(handler: (Arguments) -> Unit) {
        addFailHandler(handler)
    }

    fun onFail(handler: Consumer<Arguments>) {
        addFailHandler(handler)
    }


    fun fail(handler: (Arguments) -> Unit) {
        addFailHandler(handler)
    }

    fun fail(handler: Consumer<Arguments>) {
        addFailHandler(handler)
    }

    fun checkRequirement(): Boolean {
        println(conditions)
        for (x in conditions) {
            println("Data: ${x.javaClass}, ${x.isPositive(args)}")
            if (!x.isPositive(args)) {
                runFailHandler()
                return false
            }
        }
        return true
    }

    fun runFailHandler() {
        for (handler in onFail)
            handler(args)
    }

    inline fun checkRequirements(unit: Requirement.() -> Unit) {
        unit(this)
        if (!checkRequirement()) {
            // Requirement check failed! Break code flow with exception.
            throw CommandFailedException()
        }
    }
}