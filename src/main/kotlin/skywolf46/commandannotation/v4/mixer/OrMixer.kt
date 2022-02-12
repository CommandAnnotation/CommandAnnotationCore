package skywolf46.commandannotation.v4.mixer

import skywolf46.commandannotation.v4.abstraction.AbstractCommandCondition
import skywolf46.commandannotation.v4.abstraction.IConditionMixer
import skywolf46.commandannotation.v4.conditions.OrCondition

object OrMixer : IConditionMixer {
    override fun mix(first: AbstractCommandCondition, second: AbstractCommandCondition): AbstractCommandCondition {
        return OrCondition(first, second)
    }
}