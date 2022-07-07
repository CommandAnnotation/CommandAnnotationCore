package skywolf46.commandannotation.v4.data

import skywolf46.commandannotation.v4.api.abstraction.ICommandMatcher
import skywolf46.extrautility.core.data.ArgumentStorage

class CommandMatcherGenerator(
    val generatePriority: Int,
    val createPriority: Int,
    private val generator: (ArgumentStorage) -> ICommandMatcher?
) :
    Comparable<CommandMatcherGenerator> {
    override fun compareTo(other: CommandMatcherGenerator): Int {
        return generatePriority.compareTo(other.generatePriority)
    }

    fun generate(args: ArgumentStorage): ICommandMatcher? {
        return generator(args)
    }

}