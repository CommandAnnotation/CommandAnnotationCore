package skywolf46.commandannotation.v4.api.abstraction

import skywolf46.commandannotation.v4.api.conditions.LambdaCondition
import skywolf46.commandannotation.v4.api.data.Arguments

interface IRequirement {

    fun addCondition(unit: (Arguments) -> Boolean): IRequirement {
        return addCondition(LambdaCondition(unit))
    }


    fun prepareCondition(unit: (Arguments) -> Boolean): IRequirementPrepare {
        return prepareCondition(LambdaCondition(unit))
    }

    fun addCondition(condition: AbstractCommandCondition): IRequirement

    fun prepareCondition(condition: AbstractCommandCondition): IRequirementPrepare
}