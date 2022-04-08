package skywolf46.commandannotation.v4.data

import skywolf46.commandannotation.v4.api.abstraction.ICommand
import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.extrautility.util.MethodWrapper
import java.lang.reflect.Method
import java.util.function.Function

class CommandGeneratorWrapper(val handler: (Arguments) -> ICommand?) {
    companion object {
        fun fromMethod(method: Method) = fromMethod(MethodWrapper(method, null))

        fun fromMethod(method: MethodWrapper): CommandGeneratorWrapper {
            return CommandGeneratorWrapper { args ->
                method.invoke(args) as ICommand
            }
        }

    }

    constructor(handler: Function<Arguments, ICommand?>) : this({ args -> handler.apply(args) })

    fun createCommand(arguments: Arguments): ICommand? {
        return handler(arguments)
    }

}