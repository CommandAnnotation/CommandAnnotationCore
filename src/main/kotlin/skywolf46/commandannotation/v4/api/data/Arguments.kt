package skywolf46.commandannotation.v4.api.data

import skywolf46.commandannotation.v4.api.data.Requirement
import skywolf46.extrautility.data.ArgumentStorage

class Arguments(
    private val args: Array<String>,
    private val parameters: ArgumentStorage,
    private var pointer: Int = 0,
) : Cloneable {
    private val conditions = mutableListOf<() -> Unit>()
    private val preArguments = mutableListOf<String>()

    inline fun requires(unit: Requirement.() -> Unit) {
        Requirement(this).checkRequirements(unit)
    }

    @JvmOverloads
    fun length(fullLength: Boolean = false): Int {
        if (fullLength) {
            return args.size
        }
        return args.size + preArguments.size - pointer
    }

    operator fun <T : Any> get(cls: Class<T>, index: Int): T? {
        return parameters[cls].getOrNull(index)
    }

    operator fun <T : Any> get(cls: Class<T>): T? {
        return get(cls, 0)
    }


    operator fun <T : Any> get(cls: String): T? {
        return parameters[cls]
    }
}