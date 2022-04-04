package skywolf46.commandannotation.v4.api.data

class ExceptionHandler {
    private val expected = mutableSetOf<Class<out Throwable>>()

    fun expect(exception: Class<out Throwable>) {
        expected += exception
    }

    fun isExpected(throwable: Throwable): Boolean {
        return expected.contains(throwable.javaClass)
    }

    fun throwIfUnexpected(throwable: Throwable) {
        if (!isExpected(throwable))
            throw throwable
    }

    inline fun handle(unit: () -> Unit) {
        try {
            unit()
        } catch (e: Throwable) {
            throwIfUnexpected(e)
        }
    }

}