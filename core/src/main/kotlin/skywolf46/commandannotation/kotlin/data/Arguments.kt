package skywolf46.commandannotation.kotlin.data

import skywolf46.extrautility.data.ArgumentStorage
import kotlin.reflect.KClass

// Preprocessing이 true이면 최초 실행 (초기화) 실행이다.
// inline reified 호환 문제로 언더바로 private 표시.
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
        command.substring(command.indexOf(' ')).split(" ").toTypedArray())

    val preArguments = mutableListOf<Int>()

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


    fun next(): String {
        return _separated[_sysPointer++]
    }

    fun size(): Int {
        return _separated.size - _sysPointer
    }

    fun condition(str: String, unit: Arguments.() -> Unit): ArgumentCondition {
        return condition(false, str, unit)
    }

    inline fun <reified T : Any> arg(peek: Boolean = false): T? {
        return args()
    }

    inline fun <reified T : Any> arg(peek: Boolean, unit: Arguments.(T) -> Unit): ArgumentHandler {
        return args(peek, unit)
    }

    inline fun <reified T : Any> peekArg(): T? {
        return args(true)
    }

    inline fun <reified T : Any> peekArgs(): T? {
        args<T>(true) {
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
        return _storage[T::class]?.get(0) as T?
    }


    fun originalArgs(pointer: Int) = _separated[pointer + 1]


    inline fun <reified T : Any> params(unit: Arguments.(T) -> Unit) {
        params<T>()?.apply {
            unit(this@Arguments, this)
        }
    }

    inline fun <reified T : Any> params(str: String): T? {
        return _storage[str] as T?
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
        if (!parser.containsKey(T::class))
            return ArgumentHandler(null)
        try {
            if (preArguments.isNotEmpty()) {
                // Must clone
                try {
                    val temp = Arguments(_isPreprocessing, command, _storage, _separated, preArguments[0])
                    unit(temp, parser[T::class]!!.invoke(temp) as T)
                    if (!peek)
                        preArguments.removeAt(0)
                } catch (e: Throwable) {
                    return ArgumentHandler(e)
                }
                return ArgumentHandler(null)
            }
            val temp = Arguments(_isPreprocessing, command, _storage, _separated, _sysPointer)
            unit(temp, parser[T::class]!!.invoke(temp) as T)
            if (!peek) {
                this._sysPointer = temp._sysPointer
            }
        } catch (e: Throwable) {
            return ArgumentHandler(e)
        }
        return ArgumentHandler(null)
    }


    @JvmOverloads
    fun get(pointer: Int, useOriginalPointer: Boolean = false) =
        _separated[(if (useOriginalPointer) this._sysPointer else 0) + pointer]


    class ArgumentHandler(val exception: Throwable?) {
        inline infix fun except(unit: Exception.() -> Unit): ArgumentHandler {
            if (exception is Exception)
                unit(exception)
            return this
        }

        inline infix fun throws(unit: Throwable.() -> Unit): ArgumentHandler {
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

    class ArgumentIterator(private val arr: Array<String>, private val basePointer: Int) : Iterator<String> {
        private var pointer = 0
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