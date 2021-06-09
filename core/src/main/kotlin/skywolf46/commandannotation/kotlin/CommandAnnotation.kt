package skywolf46.commandannotation.kotlin

import skywolf46.commandannotation.kotlin.abstraction.ICommand
import skywolf46.commandannotation.kotlin.abstraction.ICommandCondition
import skywolf46.commandannotation.kotlin.abstraction.ICommandProvider
import skywolf46.commandannotation.kotlin.data.BaseCommandStartStorage
import skywolf46.commandannotation.kotlin.data.PreprocessorStorage
import skywolf46.extrautility.data.ArgumentStorage
import skywolf46.extrautility.util.MethodInvoker
import skywolf46.extrautility.util.MethodUtil
import java.lang.reflect.Method

object CommandAnnotation {
    private val providers = mutableMapOf<Class<out Annotation>, ICommandProvider<Annotation>>()
    private val preprocessor = mutableMapOf<Class<*>, PreprocessorStorage>()
    private val commands = mutableMapOf<String, BaseCommandStartStorage<*>>()

    fun getCommand(commandType: String, commandStart: String, vararg condition: ICommandCondition): ICommand? {
        return commands[commandType]?.get(commandStart, *condition)
    }

    @JvmStatic
    fun <T : Annotation> registerCommandProvider(annotation: Class<T>, provider: ICommandProvider<T>) {
        providers[annotation] = provider as ICommandProvider<Annotation>
    }

    @JvmStatic
    fun <T : Annotation, X : Annotation> registerPreprocessAnnotation(
        annotation: Class<T>,
        priority: Int,
        preProcessor: Class<X>,
        processingUnit: ArgumentStorage.() -> Boolean,
    ) {

    }

    @JvmStatic
    fun scanAllClass(cls: List<Class<*>>) {
        println("CommandAnnotation-Core | Scanning methods..")
        val baseFilter = MethodUtil.filter(*cls.toTypedArray())

        println("CommandAnnotation-Core | Processing marks...")

        println("CommandAnnotation-Core | Processing commands...")
        processCommands(baseFilter)
    }

    fun processMarks(mtd: MethodUtil.MethodFilter) {
        mtd
            .filter(false, *providers.keys.toTypedArray())
            .filter({
                System.err.println("Warning: Method ${method.name} in ${method.declaringClass.name} requires instance. Method marking rejected.")
            }, MethodUtil.ReflectionMethodFilter.INSTANCE_NOT_REQUIRED)
            .methods.forEach {
                for ((x, y) in providers) {
                    val annotation = it.method.getDeclaredAnnotation(x as Class<Annotation>)
                    if (annotation != null) {
                        y.generateCommand(annotation, MethodInvoker(it)).onRegister()
                    }
                }
            }
    }

    fun processMethods(mtd: MethodUtil.MethodFilter) {
        mtd
            .filter(false, *providers.keys.toTypedArray())
            .filter({
                System.err.println("Warning: Method ${method.name} in ${method.declaringClass.name} requires instance. Method marking rejected.")
            }, MethodUtil.ReflectionMethodFilter.INSTANCE_NOT_REQUIRED)
            .methods.forEach {
                for ((x, y) in providers) {
                    val annotation = it.method.getDeclaredAnnotation(x as Class<Annotation>)
                    if (annotation != null) {
                        y.generateCommand(annotation, MethodInvoker(it)).onRegister()
                    }
                }
            }
    }


    fun processCommands(methods: MethodUtil.MethodFilter) {
        methods
            .filter(false, *providers.keys.toTypedArray())
            .filter({
                System.err.println("Warning: Method ${method.name} in ${method.declaringClass.name} requires instance. Event handling rejected.")
            }, MethodUtil.ReflectionMethodFilter.INSTANCE_NOT_REQUIRED)
            .methods.forEach {
                for ((x, y) in providers) {
                    val annotation = it.method.getDeclaredAnnotation(x as Class<Annotation>)
                    if (annotation != null) {
                        y.generateCommand(annotation, MethodInvoker(it)).onRegister()
                    }
                }
            }
    }

}