package skywolf46.commandannotation.v4.util

import skywolf46.commandannotation.v4.abstraction.ICommandCondition
import skywolf46.commandannotation.v4.conditions.AndCondition
import skywolf46.commandannotation.v4.conditions.NotCondition
import skywolf46.commandannotation.v4.conditions.OrCondition

object CommandConditionUtil {
    @JvmStatic
    infix fun ICommandCondition.or(condition: ICommandCondition): ICommandCondition {
        return OrCondition(this, condition)
    }


    @JvmStatic
    infix fun ICommandCondition.and(condition: ICommandCondition): ICommandCondition {
        return AndCondition(this, condition)
    }

    @JvmStatic
    infix fun ICommandCondition.not(condition: ICommandCondition): ICommandCondition {
        return AndCondition(this, condition)
    }


    @JvmStatic
    infix fun ICommandCondition.nor(condition: ICommandCondition): ICommandCondition {
        return !or(condition)
    }


    @JvmStatic
    infix fun ICommandCondition.nand(condition: ICommandCondition): ICommandCondition {
        return !and(condition)
    }



    operator fun ICommandCondition.plus(condition: ICommandCondition): ICommandCondition {
        return and(condition)
    }


    operator fun ICommandCondition.not(): ICommandCondition {
        return NotCondition(this)
    }

}