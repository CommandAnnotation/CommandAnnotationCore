package skywolf46.commandannotation.v4.api.data

class ExceptionHandler {
    private val expected = mutableMapOf<Class<out Throwable>, (Throwable) -> Unit>()

    fun expect(exception: Class<out Throwable>) {
        expected[exception] = { }
    }

    fun <T : Throwable> expect(exception: Class<T>, unit: (T) -> Unit) {
        expected[exception] = unit as (Throwable) -> Unit
    }

    fun isExpected(throwable: Throwable): Boolean {
        return expected.contains(throwable.javaClass)
    }

    fun throwIfUnexpected(throwable: Throwable) {
        if (!isExpected(throwable))
            throw throwable
        expected[throwable::class.java]!!(throwable)
    }

    inline fun handleLambdaException(unit: () -> Unit) {
        try {
            unit()
        } catch (e: Throwable) {
            throwIfUnexpected(e)
        }
    }

}