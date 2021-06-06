package skywolf46.commandannotation.kotlin

import skywolf46.commandannotation.kotlin.abstraction.ICommand
import skywolf46.commandannotation.kotlin.abstraction.ICommandProvider
import skywolf46.commandannotation.kotlin.data.Arguments
import skywolf46.commandannotation.kotlin.data.CommandStorage
import skywolf46.commandannotation.kotlin.data.CommandWrapper
import skywolf46.commandannotation.kotlin.data.PreprocessorStorage
import skywolf46.extrautility.ExtraUtilityCore
import skywolf46.extrautility.data.ArgumentStorage
import skywolf46.extrautility.util.ClassUtil
import skywolf46.extrautility.util.MethodUtil
import skywolf46.extrautility.util.MethodWrapper
import java.util.logging.Logger

object CommandAnnotation {
    val logger = Logger.getLogger("CommandAnnotation")
    private val providers = mutableMapOf<Class<*>, ICommandProvider<*>>()
    private val preprocessor = mutableMapOf<Class<*>, PreprocessorStorage>()
    private val commands = mutableMapOf<String, CommandStorage>()

    fun getCommand(command: String): ICommand {
        val arg = Arguments(command)
        val iterator = arg.iterator()
        
    }

    @JvmStatic
    fun <T : Annotation> registerCommandProvider(annotation: Class<T>, provider: ICommandProvider<T>) {
        providers[annotation] = provider
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
    fun scanAllClass() {
        logger.info("Processing classes")
        val cls = ClassUtil.scanClass(ExtraUtilityCore.getIgnoredList())
        logger.fine("${cls.size} class scanned (${ExtraUtilityCore.getIgnoredList().size} packages ignored)")
        logger.fine("Scanning classes..")
        val filter = MethodUtil.filter(*cls.toTypedArray())
            .filter(MethodUtil.ReflectionMethodFilter.INSTANCE_NOT_REQUIRED)
        filter
            .filter(false, *providers.keys.toTypedArray() as Array<Class<out Annotation>>)
            .filter({

            }, MethodUtil.ReflectionMethodFilter.INSTANCE_NOT_REQUIRED)
            .methods
            .forEach {
                for (x in providers.keys) {
                    val annot = it.method.getDeclaredAnnotation(x as Class<Annotation>)
                    if (annot != null) {

                    }
                }
            }
    }

    private fun registerCommand(method: MethodWrapper, annotation: Annotation, command: ICommand) {
        val wrapper = CommandWrapper(command)
        if (preprocessor.containsKey(annotation.javaClass)) {
            preprocessor[annotation.javaClass]!!.getKeys()
        }
    }
}