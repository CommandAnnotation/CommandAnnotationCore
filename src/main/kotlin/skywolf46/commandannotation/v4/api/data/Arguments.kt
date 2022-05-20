package skywolf46.commandannotation.v4.api.data

import skywolf46.commandannotation.v4.api.annotations.debug.AddonDevelopmentMethod
import skywolf46.commandannotation.v4.api.exceptions.CommandRequirementFailedException
import skywolf46.commandannotation.v4.api.util.PeekingIterator
import skywolf46.commandannotation.v4.api.util.deserialize
import skywolf46.extrautility.data.ArgumentStorage
import kotlin.reflect.KClass

/**
 * Command meta data storage.
 */
class Arguments(
    private val args: Array<String>,
    val parameters: ArgumentStorage,
    private var preArgumentPointer: Int = 0,
    private var pointer: Int = 0,
    private val preArguments: MutableList<Any> = mutableListOf()
) : Cloneable {

    val rootHandler = ExceptionHandler()

    val temporaryHandler: ExceptionHandler = ExceptionHandler()

    @AddonDevelopmentMethod
    fun requireCurrentState(unit: Arguments.() -> Boolean) {
        if (!unit())
            throw CommandRequirementFailedException()
    }

    @AddonDevelopmentMethod
    fun expectCurrentState(exception: Class<out Throwable>) {
        temporaryHandler!!.expect(exception)
    }


    fun requires(unit: Requirement.() -> Unit) {
        Requirement(this).checkRequirements(unit)
    }

    fun <T : Any> getParameter(cls: Class<T>): T? {
        return parameters[cls].run {
            if (isEmpty())
                null
            else
                this[0]
        }
    }

    fun <T : Any> requireParameter(cls: Class<T>): T {
        return getParameter(cls) ?: throw NullPointerException("No parameter provided for class ${cls.name}")
    }

    fun createExceptionHandler(vararg expectedExceptions: Class<out Throwable>): ExceptionHandler {
        return ExceptionHandler().apply {
            expectedExceptions.forEach {
                expect(it)
            }
        }
    }

    fun expect(vararg expectedExceptions: Class<out Throwable>, unit: Arguments.() -> Unit) {
        createExceptionHandler(*expectedExceptions).apply {
            handleLambdaException {
                unit(this@Arguments)
            }
        }
    }

    inline fun <reified T : Throwable> expect(unit: Arguments.() -> Unit) {
        val handler = createExceptionHandler(T::class.java)
        try {
            unit()
        } catch (e: Throwable) {
            handler.throwIfUnexpected(e)
        }
    }

    fun <T : Throwable> handle(expectedException: Class<out T>, unit: (T) -> Unit) {
        rootHandler.expect(expectedException, unit)
    }

    inline fun <reified T : Throwable> handle(noinline unit: (T) -> Unit) {
        handle(T::class.java, unit)
    }

    fun peekArg(): String? {
        if (length(true) <= pointer)
            return null
        // Pure string pre-argument will be disabled
        //        if (preArguments.size > pointer)
        //            return preArguments[pointer]
        return args[pointer]
    }

    // Required suppression for providing generics argument
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> next(cls: KClass<T>): T? {
        if (preArguments.size > preArgumentPointer) {
            return preArguments[preArgumentPointer++] as T?
        }
        temporaryHandler.clear()
        try {
            return (temporaryHandler.handleLambdaException {
                cls.deserialize(this)
            } as T?).apply {
                println("Test: $this")
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            throw e
        }
    }

    fun <T : Any> next(cls: Class<T>): T? {
        return next(cls.kotlin)
    }

    inline fun <reified T : Any> next(): T? {
        return next(T::class)
    }


    inline fun <T : Any> next(cls: KClass<T>, unit: Arguments.(T) -> Unit) {
        try {
            val nextArg = clone()
            unit.invoke(nextArg, nextArg.next(cls) as T)
        } catch (e: Throwable) {
            // TODO : Add no-arg or exception handler
            throw e
        }
    }

    inline fun <reified T : Any> next(unit: Arguments.(T) -> Unit) {
        next(T::class, unit)
    }

    fun arg(): String? {
        if (length(true) <= pointer)
            return null
        return args[pointer++]
    }


    inline fun <reified T : Any> param() = getParameter(T::class.java)

    inline fun <reified T : Any> requireParam() = requireParameter(T::class.java)


    @JvmOverloads
    fun length(fullLength: Boolean = false): Int {
        if (fullLength) {
            return args.size
        }
        return args.size - pointer
    }


    operator fun <T : Any> get(cls: Class<T>, index: Int): T? {
        return parameters[cls].getOrNull(index)
    }

    operator fun <T : Any> get(cls: Class<T>): T? {
        return get(cls, 0)
    }


    operator fun <T : Any> get(cls: String): T? {
        return parameters[cls]
    }

    fun iterator(): PeekingIterator<String> {
        return PeekingIterator(args, pointer)
    }

    fun position(position: Int) {
        this.pointer = position
    }

    fun appendArgument(any: Any) {
        preArguments += any
    }

    public override fun clone(): Arguments {
        return Arguments(args, parameters, preArgumentPointer, pointer, preArguments)
    }
}