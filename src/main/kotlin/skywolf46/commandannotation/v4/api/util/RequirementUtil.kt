package skywolf46.commandannotation.v4.api.util

import skywolf46.commandannotation.v4.api.abstraction.IRequirement
import skywolf46.commandannotation.v4.api.abstraction.IRequirementPrepare
import skywolf46.commandannotation.v4.api.conditions.FailedCondition
import skywolf46.commandannotation.v4.api.conditions.NotCondition
import skywolf46.commandannotation.v4.api.data.Arguments
import kotlin.reflect.KClass

object RequirementUtil {

    infix fun IRequirement.length(length: Int): IRequirementPrepare {
        return prepareCondition {
            it.length() == length
        }
    }


    infix fun IRequirement.minLength(length: Int): IRequirementPrepare {
        return prepareCondition {
            it.length() >= length
        }
    }


    infix fun IRequirement.maxLength(length: Int): IRequirementPrepare {
        return prepareCondition {
            it.length() <= length
        }
    }

    infix fun IRequirement.exists(clazz: KClass<*>): IRequirementPrepare {
        return prepareCondition {
            it[clazz.java] != null
        }
    }

    operator fun IRequirement.not(): IRequirementPrepare {
        return replaceCondition {
            NotCondition(it)
        }
    }


    infix fun IRequirementPrepare.fail(unit: Arguments.() -> Unit): IRequirementPrepare {
        return replaceCondition {
            FailedCondition(it, unit)
        }
    }
}