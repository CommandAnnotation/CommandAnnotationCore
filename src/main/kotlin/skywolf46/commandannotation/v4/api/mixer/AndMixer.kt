package skywolf46.commandannotation.v4.api.mixer

import skywolf46.commandannotation.v4.api.abstraction.AbstractCommandCondition
import skywolf46.commandannotation.v4.api.abstraction.IConditionMixer
import skywolf46.commandannotation.v4.api.conditions.AndCondition

object AndMixer : IConditionMixer {
    override fun mix(first: AbstractCommandCondition, second: AbstractCommandCondition): AbstractCommandCondition {
        return AndCondition(first, second)
    }
}