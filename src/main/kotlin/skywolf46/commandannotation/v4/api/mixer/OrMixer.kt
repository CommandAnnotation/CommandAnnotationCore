package skywolf46.commandannotation.v4.api.mixer

import skywolf46.commandannotation.v4.api.abstraction.AbstractCommandCondition
import skywolf46.commandannotation.v4.api.abstraction.IConditionMixer
import skywolf46.commandannotation.v4.api.conditions.OrCondition

object OrMixer : IConditionMixer {
    override fun mix(first: AbstractCommandCondition, second: AbstractCommandCondition): AbstractCommandCondition {
        return OrCondition(first, second)
    }
}