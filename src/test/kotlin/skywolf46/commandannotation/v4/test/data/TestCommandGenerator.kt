package skywolf46.commandannotation.v4.test.data

import skywolf46.commandannotation.v4.api.annotations.define.AnnotationConverter
import skywolf46.commandannotation.v4.api.annotations.define.ArgumentGenerator
import skywolf46.commandannotation.v4.api.annotations.define.CommandGenerator
import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.extrautility.core.data.ArgumentStorage
import skywolf46.extrautility.core.util.ReflectionUtil

object TestCommandGenerator {
    @ArgumentGenerator(bindAt = [TestCommandAnnotation::class])
    fun generateArguments(args: ArgumentStorage, command: String): Arguments {
        val split = command.split(" ")
        return Arguments(split.toTypedArray(), args)
    }

    @CommandGenerator(commandAnnotation = TestCommandAnnotation::class)
    fun generateCommand(worker: ReflectionUtil.CallableFunction) : TestCommandInstance {
        return TestCommandInstance(worker)
    }

    @AnnotationConverter
    fun convert(target: TestCommandAnnotation) : TestCommandInfo {
        return TestCommandInfo(listOf(target.command))
    }

}