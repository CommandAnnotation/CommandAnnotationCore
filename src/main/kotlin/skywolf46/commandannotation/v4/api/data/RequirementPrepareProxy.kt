package skywolf46.commandannotation.v4.api.data

import skywolf46.commandannotation.v4.api.abstraction.AbstractCommandCondition
import skywolf46.commandannotation.v4.api.abstraction.IConditionMixer
import skywolf46.commandannotation.v4.api.abstraction.IRequirement
import skywolf46.commandannotation.v4.api.abstraction.IRequirementPrepare

class RequirementPrepareProxy(
    val original: Requirement,
) : IRequirementPrepare {
    override fun mixWith(mixer: IConditionMixer): IRequirementPrepare {
        original.mixCondition(mixer)
        return this
    }

    override fun addCondition(condition: AbstractCommandCondition): IRequirement {
        return original.addCondition(condition)
    }

    override fun prepareCondition(condition: AbstractCommandCondition): IRequirementPrepare {
        return this
    }

    override fun replaceCondition(conditionProvider: (AbstractCommandCondition) -> AbstractCommandCondition): IRequirementPrepare {
        return original.replaceCondition(conditionProvider)
    }
}