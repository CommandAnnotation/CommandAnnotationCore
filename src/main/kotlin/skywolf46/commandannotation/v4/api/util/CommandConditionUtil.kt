package skywolf46.commandannotation.v4.api.util

import skywolf46.commandannotation.v4.api.abstraction.IRequirement
import skywolf46.commandannotation.v4.api.abstraction.IRequirementPrepare
import skywolf46.commandannotation.v4.api.mixer.AndMixer
import skywolf46.commandannotation.v4.api.mixer.ButMixer
import skywolf46.commandannotation.v4.api.mixer.NorMixer
import skywolf46.commandannotation.v4.api.mixer.OrMixer

object CommandConditionUtil {

    infix fun IRequirementPrepare.and(require: IRequirement): IRequirementPrepare {
        return mixWith(AndMixer)
    }

    infix fun IRequirementPrepare.or(require: IRequirement): IRequirementPrepare {
        return mixWith(OrMixer)
    }

    infix fun IRequirementPrepare.nor(require: IRequirement): IRequirementPrepare {
        return mixWith(NorMixer)
    }

    infix fun IRequirementPrepare.but(require: IRequirement): IRequirementPrepare {
        return mixWith(ButMixer)
    }
}