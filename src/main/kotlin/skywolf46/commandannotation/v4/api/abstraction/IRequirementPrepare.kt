package skywolf46.commandannotation.v4.api.abstraction

interface IRequirementPrepare : IRequirement {
    fun mixWith(mixer: IConditionMixer): IRequirementPrepare
}