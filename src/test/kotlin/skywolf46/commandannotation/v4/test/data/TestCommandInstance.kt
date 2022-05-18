package skywolf46.commandannotation.v4.test.data

import skywolf46.commandannotation.v4.api.abstraction.ICommand
import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.extrautility.data.ArgumentStorage
import skywolf46.extrautility.util.MethodInvoker

class TestCommandInstance(val worker: MethodInvoker) : ICommand {
    override fun invokeCommand(arguments: Arguments) {
        try {
            worker.invoke(ArgumentStorage().apply {
                addArgument(arguments)
                addProxy(arguments.parameters)
            })
        } catch (e: Exception) {
            if (e.cause != null)
                throw e.cause!!
            throw e
        }
    }

    override fun getPriority(): Int {
        return 0
    }

    override fun toString(): String {
        return worker.method.name
    }
}