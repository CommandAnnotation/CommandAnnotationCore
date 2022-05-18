package skywolf46.commandannotation.v4.api.abstraction

interface IConditionMixer {
    fun mix(first: AbstractCommandCondition, second: AbstractCommandCondition): AbstractCommandCondition
}