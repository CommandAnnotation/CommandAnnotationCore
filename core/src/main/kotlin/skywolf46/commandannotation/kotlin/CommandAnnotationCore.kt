package skywolf46.commandannotation.kotlin

import skywolf46.commandannotation.kotlin.abstraction.ICommand
import skywolf46.commandannotation.kotlin.abstraction.ICommandCondition
import skywolf46.commandannotation.kotlin.abstraction.ICommandProvider
import skywolf46.commandannotation.kotlin.annotation.Mark
import skywolf46.commandannotation.kotlin.data.Arguments
import skywolf46.commandannotation.kotlin.data.BaseCommandStartStorage
import skywolf46.commandannotation.kotlin.data.MarkManager
import skywolf46.commandannotation.kotlin.data.PreprocessorStorage
import skywolf46.extrautility.util.ClassUtil
import skywolf46.extrautility.util.MethodInvoker
import skywolf46.extrautility.util.MethodUtil

object CommandAnnotationCore {
    internal val providers = mutableMapOf<Class<out Annotation>, ICommandProvider<Annotation>>()
    internal val preprocessors = PreprocessorStorage()
    internal val commands = mutableMapOf<String, BaseCommandStartStorage<*>>()
    val markManager = MarkManager()

    fun getCommand(commandType: String, commandStart: String, vararg condition: ICommandCondition): List<ICommand> {
        return commands[commandType]?.get(commandStart, *condition) ?: emptyList()
    }

    @JvmStatic
    fun <T : Annotation> registerCommandProvider(annotation: Class<T>, provider: ICommandProvider<T>) {
        providers[annotation] = provider as ICommandProvider<Annotation>
    }

    @JvmStatic
    fun <T : Annotation> registerPreprocessAnnotation(
        annotation: Class<T>,
        priority: Int,
        processingUnit: Arguments.(T) -> Boolean,
    ) {
        preprocessors.register(annotation as Class<Annotation>,
            priority,
            processingUnit as Arguments.(Annotation) -> Boolean)
    }

    @JvmStatic
    fun scanAllClass(cls: List<Class<*>>, log: Boolean = true) {
        var timer = System.currentTimeMillis()
//        if (log) {
//            for (x in cls) {
//            }
//        }
        if (log) {
            println("CommandAnnotation-Core | Scanning methods..")
        }
        timer = System.currentTimeMillis()
        val baseFilter = MethodUtil.filter(*cls.toTypedArray())
        if (log) {
            println("CommandAnnotation-Core | ...Completed on ${System.currentTimeMillis() - timer}ms")
            println("CommandAnnotation-Core | Processing marks...")
        }
        timer = System.currentTimeMillis()
        processMarks(baseFilter)
        if (log) {
            println("CommandAnnotation-Core | ...Completed on ${System.currentTimeMillis() - timer}ms")
            println("CommandAnnotation-Core | Processing commands...")
        }
        timer = System.currentTimeMillis()
        processCommands(baseFilter)
        if (log) {
            println("CommandAnnotation-Core | ...Completed on ${System.currentTimeMillis() - timer}ms")
        }
    }

    fun processMarks(mtd: MethodUtil.MethodFilter) {
        mtd.filter(Mark::class.java)
            .filter({
                System.err.println("Warning: Method ${method.name} in ${method.declaringClass.name} requires instance. Method marking rejected.")
            }, MethodUtil.ReflectionMethodFilter.INSTANCE_NOT_REQUIRED)
            .methods.forEach {
                markManager.registerMark(MethodInvoker(it))
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