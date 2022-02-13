package skywolf46.commandannotation.v4.api.util

import skywolf46.commandannotation.v4.api.abstraction.IRequirement
import skywolf46.commandannotation.v4.api.abstraction.IRequirementPrepare

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