package skywolf46.commandannotation.v4.data

import skywolf46.commandannotation.v4.api.abstraction.ICommand
import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.extrautility.data.ArgumentStorage
import skywolf46.extrautility.util.MethodWrapper
import java.lang.reflect.Method
import java.util.function.BiFunction

class ArgumentGeneratorWrapper(val handler: ArgumentStorage.(String) -> Arguments) {
    companion object {
        fun fromMethod(method: Method) = fromMethod(MethodWrapper(method, null))

        fun fromMethod(method: MethodWrapper): CommandGeneratorWrapper {
            return CommandGeneratorWrapper { args ->
                method.invoke(args) as ICommand
            }
        }

    }

    constructor(handler: BiFunction<ArgumentStorage, String, Arguments>) : this({
        handler.apply(this, it)
    })

    fun createArguments(storage: ArgumentStorage, command: String): Arguments {
        return handler(storage, command)
    }

}