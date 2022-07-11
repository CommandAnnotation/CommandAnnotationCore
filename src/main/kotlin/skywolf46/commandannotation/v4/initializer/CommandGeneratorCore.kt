package skywolf46.commandannotation.v4.initializer

import skywolf46.commandannotation.v4.api.abstraction.ICommand
import skywolf46.commandannotation.v4.api.abstraction.ICommandInfo
import skywolf46.commandannotation.v4.api.annotations.ArgumentRemapper
import skywolf46.commandannotation.v4.api.annotations.debug.AddonDevelopmentMethod
import skywolf46.commandannotation.v4.api.annotations.define.AnnotationConverter
import skywolf46.commandannotation.v4.api.annotations.define.AnnotationReducer
import skywolf46.commandannotation.v4.api.annotations.define.ArgumentGenerator
import skywolf46.commandannotation.v4.api.annotations.define.CommandGenerator
import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.commandannotation.v4.data.*
import skywolf46.commandannotation.v4.util.ClassUtilTemp
import skywolf46.extrautility.core.data.ArgumentStorage
import skywolf46.extrautility.core.enumeration.reflection.MethodFilter
import skywolf46.extrautility.core.util.AutoRegistrationUtil
import skywolf46.extrautility.core.util.ReflectionUtil
import skywolf46.extrautility.core.util.asCallable
import skywolf46.extrautility.core.util.asSingletonCallable
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
object CommandGeneratorCore {

    private val argumentGenerator = mutableMapOf<KClass<*>, ArgumentGeneratorWrapper>()
    private val commandGenerator = mutableMapOf<KClass<*>, CommandGeneratorWrapper>()
    private val annotationConverter = mutableMapOf<KClass<*>, AnnotationConverterWrapper<*>>()
    private val annotationReducer = mutableMapOf<KClass<*>, AnnotationReducerWrapper<*>>()
    private val arguementRemapper = mutableMapOf<String, ArgumentRemapperWrapper>()

    internal fun init() {
        scanSetupAnnotation()
    }

    private fun scanSetupAnnotation() {
        scanArgumentGeneratorAnnotation()
        scanConverterAnnotation()
        scanReducerAnnotation()
        scanArgumentRemapperAnnotation()
        scanCommandGeneratorAnnotation()
    }

    private fun scanConverterAnnotation() {
        AutoRegistrationUtil.getMethodCache().requires(AnnotationConverter::class.java)
            .filter(MethodFilter.INSTANCE_NOT_REQUIRED)
            .forEach {
                registerConverter(it.asSingletonCallable())
                println(
                    "CommandAnnotation-Generator | Registered annotation converter from method ${
                        it.asCallable().getFullName()
                    }"
                )
            }
    }

    private fun scanReducerAnnotation() {
        AutoRegistrationUtil.getMethodCache().requires(AnnotationReducer::class.java)
            .filter(MethodFilter.INSTANCE_NOT_REQUIRED)
            .forEach {
                registerReducer(it.asSingletonCallable())
                println(
                    "CommandAnnotation-Generator | Registered annotation reducer from method ${
                        it.asCallable().getFullName()
                    }"
                )
            }
    }

    private fun scanArgumentGeneratorAnnotation() {
        AutoRegistrationUtil.getMethodCache().requires(ArgumentGenerator::class.java)
            .filter(MethodFilter.INSTANCE_NOT_REQUIRED)
            .forEach { method ->
                method.getAnnotation(ArgumentGenerator::class.java).bindAt.forEach { annotation ->
                    registerArgumentGenerator(annotation.java, method.asSingletonCallable())
                    println(
                        "CommandAnnotation-Generator | Registered argument generator for \'${annotation.simpleName}\' from method ${
                            method.asCallable().getFullName()
                        }"
                    )
                }
            }
    }

    private fun scanCommandGeneratorAnnotation() {
        AutoRegistrationUtil.getMethodCache().requires(CommandGenerator::class.java)
            .filter(MethodFilter.INSTANCE_NOT_REQUIRED)
            .forEach {
                registerCommandGenerator(it.asSingletonCallable())
                val annotation = it.getAnnotation(CommandGenerator::class.java)!!
                println(
                    "CommandAnnotation-Generator | Registered command generator for \'${annotation.commandAnnotation.simpleName}\' from method (${
                        it.asCallable().getFullName()
                    })"
                )
            }
    }

    private fun scanArgumentRemapperAnnotation() {
        AutoRegistrationUtil.getMethodCache().requires(ArgumentRemapper::class.java)
            .filter(MethodFilter.INSTANCE_NOT_REQUIRED)
            .forEach { method ->
                if (method.parameterCount != 1 || method.parameterTypes[0] != String::class.java) {
                    throw IllegalStateException(
                        "Cannot register argument remapper method ${
                            method.asCallable().getFullName()
                        } : Parameter count is not 1; Remapper method must have only one String parameter to remap"
                    )
                }
                val callable = method.asSingletonCallable()
                method.getAnnotation(ArgumentRemapper::class.java).value.forEach { target ->
                    println(
                        "CommandAnnotation-Generator | Registered argument remapper for \'${target}\' from method (${
                            method.asCallable().getFullName()
                        })"
                    )
                    registerArgumentRemapper<Any>(target, ArgumentRemapperWrapper {
                        callable.invoke(listOf(it))
                    })
                }
            }
    }

    @AddonDevelopmentMethod
    fun registerConverter(method: ReflectionUtil.CallableFunction) {
        if (!ICommandInfo::class.java.isAssignableFrom(method.returnType())) {
            throw IllegalStateException("Cannot register converter method ${method.getFullName()} : Return type not implements ICommandInfo")
        }

        if (method.parameterCount() != 1) {
            throw IllegalStateException("Cannot register converter method ${method.getFullName()} : Parameter count must have to 1")
        }

        if (!Annotation::class.java.isAssignableFrom(method.parameter()[0].type)) {
            throw IllegalStateException("Cannot register converter method ${method.getFullName()} : Parameter type not implements Annotation")
        }


        registerConverter(method.parameter()[0].type as Class<Annotation>, AnnotationConverterWrapper {
            return@AnnotationConverterWrapper method.invoke(listOf(it))
                ?: throw IllegalStateException("Cannot convert ${it.javaClass.name} to ${method.returnType().name} : Return value is null from method ${method.getFullName()}")
        })
    }

    @AddonDevelopmentMethod
    fun registerReducer(method: ReflectionUtil.CallableFunction) {
        if (method.parameterCount() != 2) {
            throw IllegalStateException("Cannot register reducer method ${method.getFullName()} : Parameter count must have to 2")
        }
        val returnType = method.returnType()
        if (returnType == Void.TYPE) {
            throw IllegalStateException("Cannot register reducer method ${method.getFullName()} : Return type is void or Unit; Reducer method must have return type")
        }
        val firstParamType = method.parameter()[0].type
        val secondParameterType = method.parameter()[1].type
        if (firstParamType != secondParameterType) {
            throw IllegalStateException("Cannot register reducer method ${method.getFullName()} : Parameter type not matched; Reducer not allows different type of parameter")
        }
        if (returnType != firstParamType) {
            throw IllegalStateException("Cannot register reducer method ${method.getFullName()} : Return type not matched with parameters")
        }
        registerReducer(returnType.kotlin as KClass<Any>, AnnotationReducerWrapper {
            return@AnnotationReducerWrapper method.invoke(listOf(this, it))
                ?: throw IllegalStateException("Cannot reduce ${it.javaClass.name} : Return value is null from method ${method.getFullName()}")
        })
    }


    @AddonDevelopmentMethod
    fun registerCommandGenerator(method: ReflectionUtil.CallableFunction) {
        val annotation = method.getAnnotation<CommandGenerator>()
            ?: throw IllegalStateException("Cannot register command generator method ${method.getFullName()} : Method not contains @CommandGenerator annotation")
        val returnType = method.returnType()
        if (!ICommand::class.java.isAssignableFrom(returnType)) {
            throw IllegalStateException("Cannot register command generator method ${method.getFullName()} : Return type is not implements ICommand")
        }
        registerCommandGenerator(annotation.commandAnnotation as KClass<Any>, CommandGeneratorWrapper {
            return@CommandGeneratorWrapper method.asAutoMatchingFunction().execute(it.parameters) as ICommand?
                ?: throw IllegalStateException("Cannot generate command ${it.javaClass.name} : Return value is null from method ${method.getFullName()}")
        })
    }

    @AddonDevelopmentMethod
    fun registerArgumentGenerator(type: Class<out Annotation>, method: ReflectionUtil.CallableFunction) {
        val returnType = method.returnType()
        if (!Arguments::class.java.isAssignableFrom(returnType)) {
            throw IllegalStateException("Cannot register argument generator method ${method.getFullName()} : Return type is not implements ICommand")
        }
        val invoker = method.asAutoMatchingFunction()
        registerArgumentGenerator(type.kotlin, ArgumentGeneratorWrapper {
            return@ArgumentGeneratorWrapper invoker.execute(
                ArgumentStorage().addProxy(this).add(it).add(this)
            ) as Arguments?
                ?: throw IllegalStateException("Cannot generate Arguments instance ${it.javaClass.name} : Return value is null from method ${method.getFullName()}")
        })
    }

    @AddonDevelopmentMethod
    fun <T : Annotation> registerConverter(cls: KClass<T>, converter: AnnotationConverterWrapper<T>) {
        this.annotationConverter[cls] = converter
    }

    @AddonDevelopmentMethod
    fun <T : Annotation> registerConverter(cls: Class<T>, converter: AnnotationConverterWrapper<T>) {
        registerConverter(cls.kotlin, converter)
    }

    @AddonDevelopmentMethod
    fun <T : Any> registerReducer(cls: KClass<T>, reducer: AnnotationReducerWrapper<T>) {
        this.annotationReducer[cls] = reducer
    }

    @AddonDevelopmentMethod
    fun <T : Any> registerReducer(cls: Class<T>, reducer: AnnotationReducerWrapper<T>) {
        registerReducer(cls.kotlin, reducer)
    }

    @AddonDevelopmentMethod
    fun <T : Annotation> registerArgumentGenerator(cls: KClass<out T>, generator: ArgumentGeneratorWrapper) {
        this.argumentGenerator[cls] = generator
    }

    @AddonDevelopmentMethod
    fun <T : Annotation> registerArgumentGenerator(cls: Class<out T>, generator: ArgumentGeneratorWrapper) {
        registerArgumentGenerator(cls.kotlin, generator)
    }

    @AddonDevelopmentMethod
    fun <T : Any> registerCommandGenerator(cls: KClass<T>, generator: CommandGeneratorWrapper) {
        this.commandGenerator[cls] = generator
    }

    @AddonDevelopmentMethod
    fun <T : Any> registerArgumentRemapper(name: String, remapper: ArgumentRemapperWrapper) {
        this.arguementRemapper[name] = remapper
    }

    @AddonDevelopmentMethod
    fun <T : Any> registerCommandGenerator(cls: Class<T>, generator: CommandGeneratorWrapper) {
        registerCommandGenerator(cls.kotlin, generator)
    }

    @AddonDevelopmentMethod
    fun getRegisteredCommandAnnotations(): List<Class<Annotation>> {
        return commandGenerator.keys.map { x -> x.java }.toList() as List<Class<Annotation>>
    }

    fun <T : ICommandInfo> convert(annotation: Annotation): T {
        return ((annotationConverter[annotation.annotationClass] as AnnotationConverterWrapper<Annotation>?)?.convert(
            annotation
        )
            ?: throw IllegalStateException("Converter not registered")) as T
    }

    fun <T : ICommandInfo> reduce(first: T, second: T): T {
        val intersection = ClassUtilTemp.findClassIntersection(first::class.java, second::class.java)
        if (intersection.isEmpty()) {
            throw IllegalStateException("Cannot reduce classes; Class ${first::class.java.name} and ${second::class.java.name} not has intersection")
        }
        for (x in intersection.indices) {
            val reducerWrapper = annotationReducer[intersection[x].kotlin]
            if (reducerWrapper != null) {
                return (reducerWrapper as AnnotationReducerWrapper<Any>).reduce(first, second) as T
            }
        }
        throw IllegalStateException("Cannot reduce classes; No reducer found between class ${first::class.java.name} and ${second::class.java.name}")
    }

    fun generateArgument(type: KClass<out Annotation>, storage: ArgumentStorage, str: String): Arguments? {
        return argumentGenerator[type]?.createArguments(storage, str)
    }

    fun createCommand(type: KClass<out Annotation>, args: Arguments): ICommand? {
        return commandGenerator[type]?.createCommand(args)
    }

    fun remap(type: String, target: String): Any? {
        return arguementRemapper[type]?.remapper?.invoke(target)
    }

}