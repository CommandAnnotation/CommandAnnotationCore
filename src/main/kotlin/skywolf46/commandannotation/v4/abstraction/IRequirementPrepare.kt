package skywolf46.commandannotation.v4.abstraction

interface IRequirementPrepare : IRequirement {
    fun mixWith(mixer: IConditionMixer): IRequirementPrepare
}