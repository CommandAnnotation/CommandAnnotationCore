package skywolf46.commandannotation.kotlin.util

import skywolf46.commandannotation.kotlin.abstraction.ICommandCondition
import skywolf46.commandannotation.kotlin.impl.DoubleCommandCondition
import skywolf46.commandannotation.kotlin.impl.FixedStringCondition
import skywolf46.commandannotation.kotlin.impl.IntCommandCondition
import skywolf46.commandannotation.kotlin.impl.StringCommandCondition
import java.lang.IllegalStateException

object CommandInspector {
    private val registeredConditions: MutableMap<String, ICommandCondition> = mutableMapOf()

    init {
        registerCondition("str", StringCommandCondition())
        registerCondition("string", StringCommandCondition())
        registerCondition("int", IntCommandCondition())
        registerCondition("double", DoubleCommandCondition())
    }

    fun registerCondition(name: String, condition: ICommandCondition) {
        registeredConditions[name] = condition
    }

    fun inspect(command: String): Array<ICommandCondition> {
        val conditions = mutableListOf<ICommandCondition>()
        val split = command.split(" ")
        for (x in split) {
            val proc = x.trim()
            if (proc.startsWith('<') && proc.endsWith('>')) {
                var check = proc.substring(1, proc.length - 1)
                var parameter: String? = null
                val index = check.indexOf(':')
                if (index != -1) {
                    check = check.substring(0, index)
                    parameter = check.substring(index + 1)
                }
                registeredConditions[check]?.apply {
                    conditions += parse(parameter)
                } ?: throw IllegalStateException("Condition $check is not reigstered")
            } else {
                conditions += FixedStringCondition(proc)
            }
        }
        return conditions.toTypedArray()
    }
}