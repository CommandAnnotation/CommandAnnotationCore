package skywolf46.commandannotation.v4.initializer

import skywolf46.commandannotation.v4.api.abstraction.ICommandInfo
import skywolf46.commandannotation.v4.api.abstraction.ICommandMatcher
import skywolf46.commandannotation.v4.api.annotations.define.CommandMatcher
import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.commandannotation.v4.data.CommandMatcherGenerator
import skywolf46.extrautility.data.ArgumentStorage
import skywolf46.extrautility.util.MethodInvoker
import skywolf46.extrautility.util.MethodUtil

object CommandCore {
    private val commandMatcher = mutableListOf<CommandMatcherGenerator>()

    fun init() {
        scanCommandMatcher()
        scanCommands()
    }

    private fun scanCommandMatcher() {
        MethodUtil.getCache().filter(CommandMatcher::class.java)
            .filter(MethodUtil.ReflectionMethodFilter.INSTANCE_NOT_REQUIRED)
            .unlockAll()
            .methods.forEach {
                if (!ICommandMatcher::class.java.isAssignableFrom(it.method.returnType)) {
                    println("CommandAnnotation-Command | Cannot register command matcher from method ${it.method.declaringClass.name}#${it.method.name} : Return type is not ICommandMatcher")
                    return@forEach
                }
                val annotation = it.method.getAnnotation(CommandMatcher::class.java)
                println("CommandAnnotation-Command | Registered command matcher from method ${it.method.declaringClass.name}#${it.method.name}")
                registerCommandMatcher(annotation.priority) { storage ->
                    MethodInvoker(it).invoke(storage) as ICommandMatcher
                }
            }
    }

    private fun scanCommands() {
        val annotations = CommandGeneratorCore.getRegisteredCommandAnnotations()
        MethodUtil.getCache().filter(false, *annotations.toTypedArray())
            .filter(MethodUtil.ReflectionMethodFilter.INSTANCE_NOT_REQUIRED)
            .unlockAll()
            .methods.forEach { method ->
                for (annotation in method.method.declaredAnnotations) {
                    if (annotations.contains(annotation.annotationClass.java)) {
                        val info = CommandGeneratorCore.convert<ICommandInfo>(annotation)
                        info.getCommand().forEach { command ->
                            registerCommand(command, annotation, info, MethodInvoker(method))
                        }
                    }
                }
            }
    }

    private fun registerCommand(command: String, annotation: Annotation, info: ICommandInfo, method: MethodInvoker) {
        val commandInstance = CommandGeneratorCore.createCommand(
            annotation.annotationClass,
            Arguments(command.split(" ").toTypedArray(), ArgumentStorage().apply {
                addArgument(method)
                addArgument(info)
            })
        )
            ?: throw IllegalStateException("Cannot register command ($command) from method ${method.method.declaringClass.name}#${method.method.name} : No command instance given")
        println("CommandAnnotation-Command | Registered command ($command) as type \'${commandInstance.javaClass.simpleName}\' from method ${method.method.declaringClass.name}#${method.method.name}")
    }

    private fun registerCommandMatcher(priority: Int, matcher: (ArgumentStorage) -> ICommandMatcher) {
        commandMatcher += CommandMatcherGenerator(priority, matcher)
    }

}