package skywolf46.commandannotation.v4.util

import skywolf46.commandannotation.v4.abstraction.AbstractCommandCondition
import skywolf46.commandannotation.v4.data.Requirement

object RequirementUtil {

    infix fun Requirement.maxLength(maxLength: Int): AbstractCommandCondition {
        return prepareCondition {
            println("MaxLen: ${maxLength < it.length(false)}")
            maxLength < it.length(false)
        }
    }


    infix fun AbstractCommandCondition.maxLength(maxLength: Int): AbstractCommandCondition {
        return requirement.replaceCondition {
            println("MaxLen: ${maxLength < it.length(false)}")
            maxLength < it.length(false)
        }
    }

    infix fun Requirement.maxOriginalLength(maxLength: Int): AbstractCommandCondition {
        return prepareCondition {
            maxLength < it.length(false)
        }
    }

    infix fun AbstractCommandCondition.maxOriginalLength(maxLength: Int): AbstractCommandCondition {
        return requirement.replaceCondition {
            maxLength < it.length(false)
        }
    }

    infix fun Requirement.minLength(minLength: Int): AbstractCommandCondition {
        return prepareCondition {
            println("MinLen: ${minLength > it.length(false)}")
            minLength > it.length(false)
        }
    }


    infix fun AbstractCommandCondition.minLength(minLength: Int): AbstractCommandCondition {
        return requirement.replaceCondition {
            println("MinLen: ${minLength > it.length(false)}")
            minLength > it.length(false)
        }
    }

    infix fun Requirement.minOriginalLength(minLength: Int): AbstractCommandCondition {
        return prepareCondition {
            minLength > it.length(false)
        }
    }

    infix fun AbstractCommandCondition.minOriginalLength(minLength: Int): AbstractCommandCondition {
        return requirement.replaceCondition {
            minLength > it.length(false)
        }
    }

    infix fun Requirement.remaining(requires: Int): AbstractCommandCondition {
        return minLength(requires)
    }


    infix fun AbstractCommandCondition.remaining(requires: Int): AbstractCommandCondition {
        return requirement.minLength(requires)
    }

    infix fun Requirement.exists(requires: Class<out Any>): AbstractCommandCondition {
        return prepareCondition {
            it[requires] != null
        }
    }

    infix fun AbstractCommandCondition.exists(requires: Class<out Any>): AbstractCommandCondition {
        return requirement.replaceCondition {
            it[requires] != null
        }
    }

    infix fun Requirement.requires(requires: Class<out Any>): AbstractCommandCondition {
        return exists(requires)
    }

    infix fun AbstractCommandCondition.requires(requires: Class<out Any>): AbstractCommandCondition {
        return requires(requires)
    }

    infix fun Requirement.missing(requires: Class<out Any>): AbstractCommandCondition {
        return prepareCondition {
            it[requires] == null
        }
    }

    infix fun AbstractCommandCondition.missing(requires: Class<out Any>): AbstractCommandCondition {
        return requirement.replaceCondition {
            it[requires] == null
        }
    }


}