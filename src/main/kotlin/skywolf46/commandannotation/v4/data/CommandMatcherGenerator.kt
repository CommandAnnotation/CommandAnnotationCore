package skywolf46.commandannotation.v4.data

import skywolf46.commandannotation.v4.api.abstraction.ICommandMatcher
import skywolf46.extrautility.data.ArgumentStorage

class CommandMatcherGenerator(val priority: Int, val generator: (ArgumentStorage) -> ICommandMatcher?) :
    Comparable<CommandMatcherGenerator> {
    override fun compareTo(other: CommandMatcherGenerator): Int {
        return priority.compareTo(other.priority)
    }

    fun generate(args: ArgumentStorage): ICommandMatcher? {
        return generator(args)
    }

}