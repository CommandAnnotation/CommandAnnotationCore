package skywolf46.commandannotation.v4.conditions

import skywolf46.commandannotation.v4.abstraction.AbstractCommandCondition
import skywolf46.commandannotation.v4.data.Arguments
import java.util.function.Function

class LambdaCondition(val unit: (Arguments) -> Boolean) : AbstractCommandCondition() {
    constructor(function: Function<Arguments, Boolean>) :
            this({ args -> function.apply(args) })

    override fun isPositive(args: Arguments): Boolean {
        return unit(args)
    }

}