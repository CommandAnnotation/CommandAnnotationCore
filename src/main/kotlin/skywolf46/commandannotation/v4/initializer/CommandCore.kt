package skywolf46.commandannotation.v4.initializer

import skywolf46.commandannotation.v4.api.abstraction.ICommand
import skywolf46.commandannotation.v4.api.abstraction.ICommandInfo
import skywolf46.commandannotation.v4.api.abstraction.ICommandMatcher
import skywolf46.commandannotation.v4.api.annotations.define.CommandMatcher
import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.commandannotation.v4.api.util.PeekingIterator
import skywolf46.commandannotation.v4.constants.CommandMatcherWrapper
import skywolf46.commandannotation.v4.data.CommandBaseStorage
import skywolf46.commandannotation.v4.data.CommandMatcherGenerator
import skywolf46.extrautility.core.data.ArgumentStorage
import skywolf46.extrautility.core.enumeration.reflection.MethodFilter
import skywolf46.extrautility.core.util.AutoRegistrationUtil
import skywolf46.extrautility.core.util.ReflectionUtil
import skywolf46.extrautility.core.util.asCallable
import skywolf46.extrautility.core.util.asSingletonCallable
import kotlin.reflect.KClass

object CommandCore {
    private val commandMatcher = object : ArrayList<CommandMatcherGenerator>() {

        override fun add(element: CommandMatcherGenerator): Boolean {
            val result = super.add(element)
            sort()
            return result
        }

    }
    private val commands = mutableMapOf<KClass<*>, CommandBaseStorage>()

    fun init() {
        scanCommandMatcher()
        scanCommands()
    }

    private fun scanCommandMatcher() {
        AutoRegistrationUtil.getMethodCache().requires(CommandMatcher::class.java)
            .filter(MethodFilter.INSTANCE_NOT_REQUIRED)
            .unlock()
            .forEach {
                if (!ICommandMatcher::class.java.isAssignableFrom(it.returnType)) {
                    println("CommandAnnotation-Command | Cannot register command matcher from method ${it.declaringClass.name}#${it.name} : Return type is not ICommandMatcher")
                    return@forEach
                }
                val annotation = it.getAnnotation(CommandMatcher::class.java)
                println(
                    "CommandAnnotation-Command | Registered command matcher from method ${
                        it.asCallable().getFullName()
                    } with generate priority ${annotation.generatePriority} and execute priority ${annotation.executePriority}"
                )
                registerCommandMatcher(annotation.generatePriority, annotation.executePriority) { storage ->
                    it.asSingletonCallable().asAutoMatchingFunction().execute(storage) as ICommandMatcher?
                }
            }
    }

    private fun scanCommands() {
        val annotations = CommandGeneratorCore.getRegisteredCommandAnnotations()
        AutoRegistrationUtil.getMethodCache().requiresAny(*annotations.toTypedArray())
            .filter(MethodFilter.INSTANCE_NOT_REQUIRED)
            .unlock()
            .forEach { method ->
                for (annotation in method.declaredAnnotations) {
                    if (annotations.contains(annotation.annotationClass.java)) {
                        val info = CommandGeneratorCore.convert<ICommandInfo>(annotation)
                        info.getCommand().forEach { command ->
                            registerCommand(command, annotation, info, method.asSingletonCallable())
                        }
                    }
                }
            }
    }

    private fun registerCommand(
        command: String,
        annotation: Annotation,
        info: ICommandInfo,
        method: ReflectionUtil.CallableFunction
    ) {
        val commandInstance = CommandGeneratorCore.createCommand(
            annotation.annotationClass,
            Arguments(command.split(" ").toTypedArray(), ArgumentStorage().add(method).add(info))
        )
            ?: throw IllegalStateException("Cannot register command ($command) from method ${method.getFullName()} : No command instance given")

        commands.getOrPut(annotation.annotationClass) { CommandBaseStorage() }
            .register(commandInstance, PeekingIterator(command.split(" ").toTypedArray()))
        println("CommandAnnotation-Command | Registered command ($command) as type \'${commandInstance.javaClass.simpleName}\' from method ${method.getFullName()}")
    }

    fun findMatcher(iterator: PeekingIterator<String>): CommandMatcherWrapper? {
        for (matcher in commandMatcher) {
            val usedIterator = iterator.clone()
            val generated = matcher.generate(ArgumentStorage().add(usedIterator))
            if (generated != null) {
                usedIterator.transferTo(iterator)
                return CommandMatcherWrapper(generated, matcher.createPriority)
            }
        }
        return null
    }

    private fun registerCommandMatcher(
        generatePriority: Int,
        executePriority: Int,
        matcher: (ArgumentStorage) -> ICommandMatcher?
    ) {
        commandMatcher += CommandMatcherGenerator(generatePriority, executePriority, matcher)
    }

    fun find(type: KClass<out Annotation>, args: Arguments): ICommand? {
        return commands[type]?.find(args)
    }
}