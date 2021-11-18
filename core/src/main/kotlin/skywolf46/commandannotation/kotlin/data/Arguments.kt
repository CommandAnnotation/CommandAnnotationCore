package skywolf46.commandannotation.kotlin.data

import skywolf46.commandannotation.kotlin.exceptions.NoArgumentProcessorException
import skywolf46.extrautility.data.ArgumentStorage
import kotlin.math.absoluteValue
import kotlin.reflect.KClass

// If _isPreprocessing is true, it's initializing.
// To evade inline reified variable problem all variable will mark as public with underscore (Direct access is deprecated)
class Arguments(
    val _isPreprocessing: Boolean,
    val command: String,
    val _storage: ArgumentStorage,
    var _separated: Array<String>,
    var _sysPointer: Int = 0,
) :
    Iterable<String> {
    companion object {
        val parser = mutableMapOf<KClass<*>, Arguments.() -> Any>()
        fun <T : Any> register(cls: KClass<T>, unit: Arguments.() -> T) {
            parser[cls] = unit
        }

        fun of(cls: KClass<*>) = parser[cls]

        init {
            register(String::class) {
                return@register next()
            }
            register(Int::class) {
                return@register next().toInt()
            }
            register(Double::class) {
                return@register next().toDouble()
            }
        }
    }

    constructor(
        isPreprocessing: Boolean,
        storage: ArgumentStorage,
        command: String,
    ) : this(
        isPreprocessing,
        command.split(" ")[0],
        storage,
        if (command.indexOf(' ') != -1) command.substring(command.indexOf(' ') + 1).split(" ")
            .toTypedArray() else emptyArray())

    val preArguments = mutableListOf<Int>()

    init {
        addParameter(this)
    }

    // TODO add pre-argument
    override fun iterator(): ArgumentIterator {
        return ArgumentIterator(_separated, _sysPointer)
    }

    fun increasePointer(cloneInstance: Boolean, args: Int = 1): Arguments {
        return if (cloneInstance) {
            Arguments(_isPreprocessing, command, _storage, _separated, _sysPointer + args)
        } else {
            _sysPointer += args
            this
        }
    }

    fun clone(): Arguments {
        return Arguments(_isPreprocessing, command, _storage, _separated, _sysPointer)
    }

    fun increasePointer(args: Int = 1): Arguments {
        return increasePointer(false, args)
    }

    fun condition(cloneInstance: Boolean, str: String, unit: Arguments.() -> Unit): ArgumentCondition {
        val cond =
            ArgumentCondition(if (cloneInstance) Arguments(_isPreprocessing,
                command,
                _storage,
                _separated,
                _sysPointer) else this)
        cond.condition(str, unit)
        return cond
    }


    fun last(): String {
        return _separated[_separated.size - 1]
    }

    fun next(): String {
        if (preArguments.isNotEmpty()) {
            return _separated[preArguments.removeAt(0)]
        }
        return _separated[_sysPointer++]
    }

    fun size(): Int {
        return _separated.size - _sysPointer + preArguments.size
    }

    fun condition(str: String, unit: Arguments.() -> Unit): ArgumentCondition {
        return condition(false, str, unit)
    }

    inline fun <reified T : Any> arg(peek: Boolean = false): T? {
        return args(T::class, peek)
    }

    inline fun <reified T : Any> arg(peek: Boolean, unit: Arguments.(T) -> Unit): ArgumentHandler {
        return args(T::class, peek, unit)
    }

    inline fun <T : Any> arg(cls: KClass<T>, peek: Boolean, unit: Arguments.(T) -> Unit): ArgumentHandler {
        return args(cls, peek, unit)
    }

    inline fun <reified T : Any> args(peek: Boolean, unit: Arguments.(T) -> Unit): ArgumentHandler {
        return args(T::class, peek, unit)
    }


    inline fun <reified T : Any> peekArg(): T? {
        return args(T::class, true)
    }

    inline fun <reified T : Any> peekArgs(): T? {
        return args(T::class, true)
    }

    fun <T : Any> args(cls: KClass<T>, peek: Boolean = false): T? {
        args(cls, peek) {
            return it
        }
        return null
    }

    inline fun <reified T : Any> args(peek: Boolean = false): T? {
        args<T>(peek) {
            return it
        }
        return null
    }


    inline fun <reified T : Any> params(): T? {
        return _storage[T::class][0]
    }


    fun originalArgs(pointer: Int) = _separated[pointer + 1]


    inline fun <reified T : Any> params(unit: Arguments.(T) -> Unit) {
        params<T>()?.apply {
            unit(this@Arguments, this)
        }
    }

    inline fun <reified T : Any> params(str: String): T? {
        return _storage[str]
    }


    inline fun <reified T : Any> params(str: String, unit: Arguments.(T) -> Unit): ArgumentHandler {
        return params<T>(str)?.let {
            unit(this, it)
            return@let ArgumentHandler(null)
        } ?:
        // TODO add no-argument handler
        ArgumentHandler(null)
    }

    fun addParameter(any: Any) {
        _storage.addArgument(any)
    }

    fun setParameter(name: String, any: Any) {
        _storage.setArgument(name, any)
    }

    inline fun <reified T : Any> Arguments.args(peek: Boolean = false, unit: Arguments.(T) -> Unit): ArgumentHandler {
        return args(T::class, peek, unit)
    }


    inline fun <X : Any> Arguments.args(
        cls: KClass<X>,
        peek: Boolean = false,
        unit: Arguments.(X) -> Unit,
    ): ArgumentHandler {
        if (!parser.containsKey(cls))
            return ArgumentHandler(NoArgumentProcessorException)
        try {
            if (preArguments.isNotEmpty()) {
                // Must clone
                try {
                    val temp = Arguments(_isPreprocessing, command, _storage, _separated, _sysPointer)
                    temp._sysPointer = _sysPointer
                    temp.preArguments.addAll(preArguments)
                    val next = parser[cls]!!.invoke(temp) as X
                    if (!peek) {
                        extractFromTemp(temp)
                    }
                    unit(temp, next)
                    if (!peek &&
                        (temp.preArguments.size != preArguments.size || temp._sysPointer != _sysPointer)
                    ) {
                        extractFromTemp(temp)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                    return ArgumentHandler(e)
                }

                return ArgumentHandler(null)
            }
            val temp = Arguments(_isPreprocessing, command, _storage, _separated, _sysPointer)
            temp.preArguments.addAll(preArguments)
            val next = parser[cls]!!.invoke(temp) as X
            if (!peek) {
                this._sysPointer = temp._sysPointer
            }
            unit(temp, next)
        } catch (e: Throwable) {
            return ArgumentHandler(e)
        }
        return ArgumentHandler(null)
    }

    @Deprecated("Inner method")
    fun extractFromTemp(temp: Arguments) {
        val diff = (temp._sysPointer - this._sysPointer).absoluteValue
        if (diff > 0) {
            this._sysPointer += diff
        }
        this.preArguments.clear()
        this.preArguments.addAll(temp.preArguments)
    }

    @JvmOverloads
    fun get(pointer: Int, useOriginalPointer: Boolean = false) =
        _separated[(if (useOriginalPointer) this._sysPointer else 0) + pointer]

    fun fullSize(): Int {
        return _separated.size + preArguments.size
    }


    class ArgumentHandler(val exception: Throwable?) {
        inline infix fun except(unit: Exception.() -> Unit): ArgumentHandler {
            if (exception is Exception)
                unit(exception)
            return this
        }

        inline infix fun noArgs(unit: NoArgumentProcessorException.() -> Unit): ArgumentHandler {
            if (exception is NoArgumentProcessorException)
                unit(exception)
            return this
        }

        inline infix fun illegalNumber(unit: NumberFormatException.() -> Unit): ArgumentHandler {
            if (exception is NumberFormatException)
                unit(exception)
            return this
        }

        inline infix fun illegalState(unit: IllegalStateException.() -> Unit): ArgumentHandler {
            if (exception is IllegalStateException)
                unit(exception)
            return this
        }

        inline infix fun throws(unit: Throwable.() -> Unit): ArgumentHandler {
            if (exception is NoArgumentProcessorException)
                return this
            exception?.apply(unit)
            return this
        }

        inline infix fun <reified T : Throwable> handle(unit: T.() -> Unit): ArgumentHandler {
            if (exception is T)
                exception.apply(unit)
            return this
        }

    }


    class ArgumentCondition(val args: Arguments) {
        private var isProceed = false
        private var currentPointer = args._sysPointer
        private var exception: Throwable? = null
        fun condition(str: String, unit: Arguments.() -> Unit): ArgumentCondition {
            if (isProceed)
                return this
            if (currentPointer >= args._separated.size)
                return this
            if (args.get(currentPointer, false) == str) {
                isProceed = true
                args.increasePointer(1)
                try {
                    unit(args)
                } catch (e: Throwable) {
                    exception = e
                }
            }
            return this
        }

        infix fun denial(unit: Arguments.() -> Unit): ArgumentCondition {
            if (!isProceed)
                unit(args)
            return this
        }

        infix fun handle(unit: Arguments.(Throwable) -> Unit): ArgumentCondition {
            if (exception != null)
                unit(args, exception!!)
            return this
        }
    }

    class ArgumentIterator(private val arr: Array<String>, val basePointer: Int) : Iterator<String> {
        var pointer = 0
            private set

        fun currentPointer(): Int {
            return pointer + basePointer
        }

        override fun hasNext(): Boolean {
            return arr.size <= pointer + basePointer
        }

        override fun next(): String {
            return arr[pointer++ + basePointer]
        }

        fun left() = arr.size - (pointer + basePointer)

        fun peekNext(skipping: Int = 0): String {
            return arr[pointer + basePointer + skipping]
        }

        fun forwardedSize() = pointer
    }

}
