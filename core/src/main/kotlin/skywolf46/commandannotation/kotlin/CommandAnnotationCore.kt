package skywolf46.commandannotation.kotlin

import skywolf46.commandannotation.kotlin.abstraction.ICommand
import skywolf46.commandannotation.kotlin.abstraction.ICommandCondition
import skywolf46.commandannotation.kotlin.abstraction.ICommandProvider
import skywolf46.commandannotation.kotlin.annotation.AutoCompleteProvider
import skywolf46.commandannotation.kotlin.annotation.CompactCondition
import skywolf46.commandannotation.kotlin.annotation.Mark
import skywolf46.commandannotation.kotlin.data.Arguments
import skywolf46.commandannotation.kotlin.data.BaseCommandStartStorage
import skywolf46.commandannotation.kotlin.data.MarkManager
import skywolf46.commandannotation.kotlin.data.PreprocessorStorage
import skywolf46.commandannotation.kotlin.impl.CompactCommandCondition
import skywolf46.commandannotation.kotlin.util.CommandInspector
import skywolf46.extrautility.data.ArgumentStorage
import skywolf46.extrautility.util.MethodInvoker
import skywolf46.extrautility.util.MethodUtil
import skywolf46.extrautility.util.MethodWrapper

object CommandAnnotationCore {
    private val providers = mutableMapOf<Class<out Annotation>, ICommandProvider<Annotation>>()
    internal val preprocessors = PreprocessorStorage()
    private val commands = mutableMapOf<String, BaseCommandStartStorage<*>>()
    val markManager = MarkManager()
    private val completerCache = ArgumentStorage()

    fun registerCompleter(name: String, completer: (ArgumentStorage) -> MutableList<String>) {
        completerCache[name] = completer
    }

    fun getCompleter(name: String) = completerCache.get<(ArgumentStorage) -> MutableList<String>>(name)

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
        preprocessors.register(annotation,
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

        val baseFilter = MethodUtil.getCache()
        processCompactCondition(baseFilter)
        timer = System.currentTimeMillis()

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
            println("CommandAnnotation-Core | Processing completer...")
        }
        timer = System.currentTimeMillis()
        processCompleter(baseFilter)
        if (log) {
            println("CommandAnnotation-Core | ...Completed on ${System.currentTimeMillis() - timer}ms")
        }

    }


    fun processCompactCondition(mtd: MethodUtil.MethodFilter) {
        mtd.filter(CompactCondition::class.java)
            .filter({}, MethodUtil.ReflectionMethodFilter.INSTANCE_NOT_REQUIRED).methods.forEach {
                val ret = it.method.returnType
                if (Array::class.java.isAssignableFrom(ret)) {
                    processCompactCompleterMethod(it)
                } else if (Boolean::class.java.isAssignableFrom(ret)) {
                    processCompactConditionMethod(it)
                }
            }
    }

    private fun processCompactConditionMethod(mtd: MethodWrapper) {
        findCondition(mtd) {
            it.conditionMethod = MethodInvoker(mtd)
        }
    }

    private fun processCompactCompleterMethod(mtd: MethodWrapper) {
        findCondition(mtd) {
            it.autoCompleteMethod = MethodInvoker(mtd)
        }
    }

    private inline fun findCondition(mtd: MethodWrapper, funct: (CompactCommandCondition) -> Unit) {
        val annot = mtd.method.getDeclaredAnnotation(CompactCondition::class.java)
        val cond = CommandInspector.getCondition(annot.completer) ?: run {
            CommandInspector.registerCondition(annot.completer, CompactCommandCondition())
            CommandInspector.getCondition(annot.completer)
        }
        if (cond !is CompactCommandCondition) {
            System.err.println("Failed to register compact condition ${annot.completer} : Condition is already registered and not compact condition")
            return
        }
        funct(cond)
    }

    fun processCompleter(mtd: MethodUtil.MethodFilter) {
        mtd.filter(AutoCompleteProvider::class.java)
            .filter({
                System.err.println("Warning: Method ${method.name} in ${method.declaringClass.name} requires instance. Autocomplete registering rejected.")
            }, MethodUtil.ReflectionMethodFilter.INSTANCE_NOT_REQUIRED)
            .methods.forEach { mtd ->
                val invoker = MethodInvoker(mtd.method, mtd.instance)
                registerCompleter(mtd.method.getAnnotation(AutoCompleteProvider::class.java).provider) {
                    invoker.invoke(it) as MutableList<String>
                }
            }
    }


    private fun processMarks(mtd: MethodUtil.MethodFilter) {
        mtd.filter(Mark::class.java)
            .filter({
                System.err.println("Warning: Method ${method.name} in ${method.declaringClass.name} requires instance. Method marking rejected.")
            }, MethodUtil.ReflectionMethodFilter.INSTANCE_NOT_REQUIRED)
            .methods.forEach {
                markManager.registerMark(MethodInvoker(it))
            }
    }


    private fun processCommands(methods: MethodUtil.MethodFilter) {
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