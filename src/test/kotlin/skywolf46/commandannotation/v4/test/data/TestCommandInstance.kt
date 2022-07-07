package skywolf46.commandannotation.v4.test.data

import skywolf46.commandannotation.v4.api.abstraction.ICommand
import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.extrautility.core.data.ArgumentStorage
import skywolf46.extrautility.core.util.ReflectionUtil

class TestCommandInstance(worker: ReflectionUtil.CallableFunction) : ICommand {
    val function = worker.asAutoMatchingFunction()
    override fun invokeCommand(arguments: Arguments) {
        try {
            function.execute(
                ArgumentStorage()
                    .add(arguments)
                    .addProxy(arguments.parameters)
            )
        } catch (e: Throwable) {
            if (e.cause != null)
                throw e.cause!!
            throw e
        }
    }

    override fun getPriority(): Int {
        return 0
    }

    override fun toString(): String {
        return function.getFullName()
    }
}