package skywolf46.commandannotation.v4.initializer

import skywolf46.commandannotation.v4.api.abstraction.ICommand
import skywolf46.commandannotation.v4.api.abstraction.ICommandInfo
import skywolf46.commandannotation.v4.api.annotations.debug.AddonDevelopmentMethod
import skywolf46.commandannotation.v4.api.annotations.define.AnnotationConverter
import skywolf46.commandannotation.v4.api.annotations.define.AnnotationReducer
import skywolf46.commandannotation.v4.api.annotations.define.ArgumentGenerator
import skywolf46.commandannotation.v4.api.annotations.define.CommandGenerator
import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.commandannotation.v4.data.AnnotationConverterWrapper
import skywolf46.commandannotation.v4.data.AnnotationReducerWrapper
import skywolf46.commandannotation.v4.data.ArgumentGeneratorWrapper
import skywolf46.commandannotation.v4.data.CommandGeneratorWrapper
import skywolf46.commandannotation.v4.util.ClassUtilTemp
import skywolf46.extrautility.data.ArgumentStorage
import skywolf46.extrautility.util.ClassUtil.iterateParentClasses
import skywolf46.extrautility.util.MethodInvoker
import skywolf46.extrautility.util.MethodUtil
import skywolf46.extrautility.util.MethodWrapper
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
object CommandGeneratorCore {

    private val argumentGenerator = mutableMapOf<KClass<*>, ArgumentGeneratorWrapper>()
    private val commandGenerator = mutableMapOf<KClass<*>, CommandGeneratorWrapper>()
    private val annotationConverter = mutableMapOf<KClass<*>, AnnotationConverterWrapper<*>>()
    private val annotationReducer = mutableMapOf<KClass<*>, AnnotationReducerWrapper<*>>()

    internal fun init() {
        scanSetupAnnotation()
    }

    private fun scanSetupAnnotation() {
        scanArgumentGeneratorAnnotation()
        scanConverterAnnotation()
        scanReducerAnnotation()
        scanCommandGeneratorAnnotation()
    }

    private fun scanConverterAnnotation() {
        MethodUtil.getCache().filter(AnnotationConverter::class.java)
            .filter(MethodUtil.ReflectionMethodFilter.INSTANCE_NOT_REQUIRED)
            .filter(MethodUtil.ReflectionMethodFilter.NOT_ABSTRACTED).methods.forEach {
                registerConverter(it)
                println("CommandAnnotation-Generator | Registered annotation converter from method ${it.method.declaringClass.name}#${it.method.name}")
            }
    }

    private fun scanReducerAnnotation() {
        MethodUtil.getCache().filter(AnnotationReducer::class.java)
            .filter(MethodUtil.ReflectionMethodFilter.INSTANCE_NOT_REQUIRED)
            .filter(MethodUtil.ReflectionMethodFilter.NOT_ABSTRACTED).methods.forEach {
                registerReducer(it)
                println("CommandAnnotation-Generator | Registered annotation reducer from method ${it.method.declaringClass.name}#${it.method.name}")
            }
    }

    private fun scanArgumentGeneratorAnnotation() {
        MethodUtil.getCache().filter(ArgumentGenerator::class.java)
            .filter(MethodUtil.ReflectionMethodFilter.INSTANCE_NOT_REQUIRED)
            .filter(MethodUtil.ReflectionMethodFilter.NOT_ABSTRACTED).methods.forEach { wrapper ->
                wrapper.method.getAnnotation(ArgumentGenerator::class.java).bindAt.forEach { annotation ->
                    registerArgumentGenerator(annotation.java, wrapper)
                    println("CommandAnnotation-Generator | Registered argument generator for \'${annotation.simpleName}\' from method ${wrapper.method.declaringClass.name}#${wrapper.method.name}")
                }
            }
    }

    private fun scanCommandGeneratorAnnotation() {
        MethodUtil.getCache().filter(CommandGenerator::class.java)
            .filter(MethodUtil.ReflectionMethodFilter.INSTANCE_NOT_REQUIRED).methods.forEach {
                registerCommandGenerator(it)
                val annotation = it.method.getAnnotation(CommandGenerator::class.java)!!
                println("CommandAnnotation-Generator | Registered command generator for \'${annotation.commandAnnotation.simpleName}\' from method (${it.method.declaringClass.name}#${it.method.name})")
            }
    }

    @AddonDevelopmentMethod
    fun registerConverter(method: MethodWrapper) {
        if (!ICommandInfo::class.java.isAssignableFrom(method.method.returnType)) {
            throw IllegalStateException("Cannot register converter method ${method.method.declaringClass.name}#${method.method.name} : Return type not implements ICommandInfo")
        }

        if (method.method.parameterCount != 1) {
            throw IllegalStateException("Cannot register converter method ${method.method.declaringClass.name}#${method.method.name} : Parameter count must have to 1")
        }

        if (!Annotation::class.java.isAssignableFrom(method.method.parameterTypes[0])) {
            throw IllegalStateException("Cannot register converter method ${method.method.declaringClass.name}#${method.method.name} : Parameter type not implements Annotation")
        }


        registerConverter(method.method.parameterTypes[0] as Class<Annotation>, AnnotationConverterWrapper {
            return@AnnotationConverterWrapper method.invoke(it)
                ?: throw IllegalStateException("Cannot convert ${it.javaClass.name} to ${method.method.returnType.name} : Return value is null from method ${method.method.declaringClass.name}#${method.method.name}")
        })
    }

    @AddonDevelopmentMethod
    fun registerReducer(method: MethodWrapper) {
        if (method.method.parameterCount != 2) {
            throw IllegalStateException("Cannot register reducer method ${method.method.declaringClass.name}#${method.method.name} : Parameter count must have to 2")
        }
        val returnType = method.method.returnType
        if (returnType.equals(Void.TYPE)) {
            throw IllegalStateException("Cannot register reducer method ${method.method.declaringClass.name}#${method.method.name} : Return type is void or Unit; Reducer method must have return type")
        }
        val firstParamType = method.method.parameterTypes[0]
        val secondParameterType = method.method.parameterTypes[1]
        if (firstParamType != secondParameterType) {
            throw IllegalStateException("Cannot register reducer method ${method.method.declaringClass.name}#${method.method.name} : Parameter type not matched; Reducer not allows different type of parameter")
        }
        if (returnType != firstParamType) {
            throw IllegalStateException("Cannot register reducer method ${method.method.declaringClass.name}#${method.method.name} : Return type not matched with parameters")
        }
        registerReducer(returnType.kotlin as KClass<Any>, AnnotationReducerWrapper {
            return@AnnotationReducerWrapper method.invoke(this, it)
                ?: throw IllegalStateException("Cannot reduce ${it.javaClass.name} : Return value is null from method ${method.method.declaringClass.name}#${method.method.name}")
        })
    }


    @AddonDevelopmentMethod
    fun registerCommandGenerator(method: MethodWrapper) {
        val annotation = method.method.getAnnotation(CommandGenerator::class.java)
            ?: throw IllegalStateException("Cannot register command generator method ${method.method.declaringClass.name}#${method.method.name} : Method not contains @CommandGenerator annotation")
        val returnType = method.method.returnType
        if (!ICommand::class.java.isAssignableFrom(returnType)) {
            throw IllegalStateException("Cannot register command generator method ${method.method.declaringClass.name}#${method.method.name} : Return type is not implements ICommand")
        }
        val invoker = MethodInvoker(method)
        registerCommandGenerator(annotation.commandAnnotation as KClass<Any>, CommandGeneratorWrapper {
            return@CommandGeneratorWrapper invoker.invoke(it.parameters) as ICommand?
                ?: throw IllegalStateException("Cannot generate command ${it.javaClass.name} : Return value is null from method ${method.method.declaringClass.name}#${method.method.name}")
        })
    }

    @AddonDevelopmentMethod
    fun registerArgumentGenerator(type: Class<out Annotation>, method: MethodWrapper) {
        val returnType = method.method.returnType
        if (!Arguments::class.java.isAssignableFrom(returnType)) {
            throw IllegalStateException("Cannot register argument generator method ${method.method.declaringClass.name}#${method.method.name} : Return type is not implements ICommand")
        }
        val invoker = MethodInvoker(method)
        registerArgumentGenerator(type.kotlin, ArgumentGeneratorWrapper {
            return@ArgumentGeneratorWrapper invoker.invoke(this.apply {
                addArgument(it)
            }) as Arguments?
                ?: throw IllegalStateException("Cannot generate Arguments instance ${it.javaClass.name} : Return value is null from method ${method.method.declaringClass.name}#${method.method.name}")
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
    fun <T : Any> registerCommandGenerator(cls: Class<T>, generator: CommandGeneratorWrapper) {
        registerCommandGenerator(cls.kotlin, generator)
    }

    @AddonDevelopmentMethod
    fun getRegisteredCommandAnnotations(): List<Class<Annotation>> {
        return commandGenerator.keys.map { x -> x.java }.toList() as List<Class<Annotation>>
    }

    fun <T : ICommandInfo> convert(annotation: Annotation): T {
        return ((annotationConverter[annotation.annotationClass] as AnnotationConverterWrapper<Annotation>?)?.convert(annotation)
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

    fun generateArgument(type: KClass<Annotation>, storage: ArgumentStorage, str: String): Arguments? {
        return argumentGenerator[type]?.createArguments(storage, str)
    }

    fun createCommand(type: KClass<out Annotation>, args: Arguments): ICommand? {
        println(commandGenerator)
        return commandGenerator[type]?.createCommand(args)
    }


}