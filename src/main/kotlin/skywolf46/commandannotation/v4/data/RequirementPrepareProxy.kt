package skywolf46.commandannotation.v4.data

import skywolf46.commandannotation.v4.abstraction.AbstractCommandCondition
import skywolf46.commandannotation.v4.abstraction.IConditionMixer
import skywolf46.commandannotation.v4.abstraction.IRequirement
import skywolf46.commandannotation.v4.abstraction.IRequirementPrepare

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
}