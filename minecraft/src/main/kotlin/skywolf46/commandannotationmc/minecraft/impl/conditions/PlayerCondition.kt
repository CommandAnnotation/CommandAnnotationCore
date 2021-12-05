package skywolf46.commandannotationmc.minecraft.impl.conditions

import org.bukkit.Bukkit
import skywolf46.commandannotation.kotlin.abstraction.ICommandCondition
import skywolf46.commandannotation.kotlin.data.Arguments
import skywolf46.extrautility.util.ifTrue

class PlayerCondition(
    // If true, not check non-player string.
    val containsAll: Boolean,
) : ICommandCondition {

    override fun parse(str: String?): ICommandCondition {
        return PlayerCondition(str == "all")
    }

    override fun isMatched(argument: Arguments, iterator: Arguments.ArgumentIterator): Boolean {
        if(!iterator.hasNext())
            return false
        val next = iterator.peek()
        return (if (containsAll)
            return true
        else Bukkit.getOnlinePlayers().any { x -> x.name.toLowerCase() == next })
    }

    override fun getConditionPriority(): Int {
        return 100
    }


    override fun findNextAutoComplete(argument: Arguments, includeSelf: Boolean): List<String> {
        val next = argument.next()
        return Bukkit.getOnlinePlayers().filter { x -> x.name.toLowerCase().startsWith(next) }.map { x -> x.name }
    }
}