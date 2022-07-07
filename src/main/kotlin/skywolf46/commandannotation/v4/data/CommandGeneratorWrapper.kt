package skywolf46.commandannotation.v4.data

import skywolf46.commandannotation.v4.api.abstraction.ICommand
import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.extrautility.core.util.ReflectionUtil
import skywolf46.extrautility.core.util.asSingletonCallable
import java.lang.reflect.Method
import java.util.function.Function

class CommandGeneratorWrapper(val handler: (Arguments) -> ICommand?) {
    companion object {
        fun fromMethod(method: Method) =
            fromMethod(method.asSingletonCallable().asAutoMatchingFunction())


        fun fromMethod(method: ReflectionUtil.AutoMatchedCallableFunction): CommandGeneratorWrapper {
            return CommandGeneratorWrapper { args ->
                method.execute(args.parameters) as ICommand
            }
        }

    }

    constructor(handler: Function<Arguments, ICommand?>) : this({ args -> handler.apply(args) })

    fun createCommand(arguments: Arguments): ICommand? {
        return handler(arguments)
    }

}