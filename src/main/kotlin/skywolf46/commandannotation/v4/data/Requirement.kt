package skywolf46.commandannotation.v4.data

import skywolf46.commandannotation.v4.abstraction.AbstractCommandCondition
import skywolf46.commandannotation.v4.annotations.debug.AddonDevelopmentMethod
import skywolf46.commandannotation.v4.annotations.debug.RequireSingleThread
import skywolf46.commandannotation.v4.conditions.LambdaCondition
import skywolf46.commandannotation.v4.util.CommandConditionUtil.and
import java.util.function.Consumer

@RequireSingleThread
class Requirement(val args: Arguments) {
    private val conditions = mutableListOf<AbstractCommandCondition>()
    private val onFail = mutableListOf<(Arguments) -> Unit>()

    private fun createCondition(unit: (Arguments) -> Boolean): AbstractCommandCondition {
        return LambdaCondition(unit)
    }

    inline fun checkRequirements(unit: Requirement.() -> Unit) {
        unit(this)
        // If requirement check failed, stop entire command with "inline" return logic
        if (!checkRequirement(args))
            return
        // If requirement check succeed, run command
    }

    @AddonDevelopmentMethod
    fun prepareCondition(baseCondition: AbstractCommandCondition): AbstractCommandCondition {
        conditions += baseCondition
        return baseCondition.apply {
            requirement = this@Requirement
        }
    }

    @AddonDevelopmentMethod
    fun prepareCondition(baseCondition: (Arguments) -> Boolean): AbstractCommandCondition {
        return prepareCondition(createCondition(baseCondition))
    }

    @AddonDevelopmentMethod
    fun andCondition(condition: AbstractCommandCondition): AbstractCommandCondition {
        condition.requirement = this
        conditions[conditions.size - 1] = conditions[conditions.size - 1].and(condition)
        return condition
    }


    @AddonDevelopmentMethod
    fun orCondition(condition: AbstractCommandCondition): AbstractCommandCondition {
        condition.requirement = this
        conditions[conditions.size - 1] = conditions[conditions.size - 1].and(condition)
        return condition
    }


    @AddonDevelopmentMethod
    fun replaceCondition(condition: AbstractCommandCondition): AbstractCommandCondition {
        condition.requirement = this
        conditions[conditions.size - 1] = condition
        return condition
    }

    @AddonDevelopmentMethod
    fun replaceCondition(condition: (Arguments) -> Boolean): AbstractCommandCondition {
        return andCondition(createCondition(condition))
    }


    fun addCondition(condition: AbstractCommandCondition): AbstractCommandCondition {
        return condition.apply {
            conditions.add(this)
        }
    }

    fun addCondition(unit: (Arguments) -> Boolean): AbstractCommandCondition {
        return addCondition(createCondition(unit))
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

    fun checkRequirement(args: Arguments): Boolean {
        println(conditions)
        for (x in conditions) {
            println("Data: ${x.javaClass}, ${x.isPositive(args)}")
            if (!x.isPositive(args)) {
                runFailHandler(args)
                return false
            }
        }
        return true
    }

    fun runFailHandler(args: Arguments) {
        for (handler in onFail)
            handler(args)
    }
}