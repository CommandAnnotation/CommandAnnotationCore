package skywolf46.commandannotation.v4.util

import skywolf46.commandannotation.v4.abstraction.AbstractCommandCondition
import skywolf46.commandannotation.v4.conditions.AndCondition
import skywolf46.commandannotation.v4.conditions.FailCheckCondition
import skywolf46.commandannotation.v4.conditions.NotCondition
import skywolf46.commandannotation.v4.conditions.OrCondition
import skywolf46.commandannotation.v4.data.Arguments

object CommandConditionUtil {


    @JvmStatic
    infix fun AbstractCommandCondition.or(condition: AbstractCommandCondition): AbstractCommandCondition {
        return OrCondition(this, condition).also {
            it.requirement = requirement
        }
    }

    @JvmStatic
    infix fun AbstractCommandCondition.and(condition: AbstractCommandCondition): AbstractCommandCondition {
        return AndCondition(this, condition).also {
            it.requirement = requirement
        }
    }

    @JvmStatic
    infix fun AbstractCommandCondition.nor(condition: AbstractCommandCondition): AbstractCommandCondition {
        return !or(condition)
    }

    @JvmStatic
    infix fun AbstractCommandCondition.nand(condition: AbstractCommandCondition): AbstractCommandCondition {
        return !and(condition)
    }

    @JvmStatic
    infix fun AbstractCommandCondition.ifFail(unit: (Arguments) -> Unit): AbstractCommandCondition {
        return FailCheckCondition(this, unit).also {
            it.requirement = requirement
        }
    }

    operator fun AbstractCommandCondition.plus(condition: AbstractCommandCondition): AbstractCommandCondition {
        return and(condition)
    }

    operator fun AbstractCommandCondition.not(): AbstractCommandCondition {
        return NotCondition(this)
    }


}