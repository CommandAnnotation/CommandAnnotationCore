package skywolf46.commandannotation.v4.util

import skywolf46.commandannotation.v4.abstraction.IRequirement
import skywolf46.commandannotation.v4.abstraction.IRequirementPrepare
import skywolf46.commandannotation.v4.data.RequirementPrepareProxy

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

}