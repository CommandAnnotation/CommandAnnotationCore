package skywolf46.commandannotation.v4.test.data

import skywolf46.commandannotation.v4.api.abstraction.ICommand
import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.extrautility.data.ArgumentStorage
import skywolf46.extrautility.util.MethodInvoker
import skywolf46.extrautility.util.MethodWrapper

class TestCommandInstance(val worker: MethodInvoker) : ICommand {
    override fun invokeCommand(arguments: Arguments) {
        worker.invoke(ArgumentStorage().apply {
            addArgument(arguments)
            addProxy(arguments.parameters)
        })
    }

    override fun getPriority(): Int {
        return 0
    }
}