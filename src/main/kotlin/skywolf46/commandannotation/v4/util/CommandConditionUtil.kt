package skywolf46.commandannotation.v4.util

import skywolf46.commandannotation.v4.abstraction.IRequirement
import skywolf46.commandannotation.v4.abstraction.IRequirementPrepare
import skywolf46.commandannotation.v4.mixer.AndMixer
import skywolf46.commandannotation.v4.mixer.OrMixer

object CommandConditionUtil {


    infix fun IRequirementPrepare.and(require: IRequirement): IRequirementPrepare {
        return mixWith(AndMixer)
    }


    infix fun IRequirementPrepare.or(require: IRequirement): IRequirementPrepare {
        return mixWith(OrMixer)
    }


}