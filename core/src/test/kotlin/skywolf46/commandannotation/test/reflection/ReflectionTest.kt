package skywolf46.commandannotation.test.reflection

import org.junit.Assert
import org.junit.Test
import skywolf46.commandannotation.kotlin.util.ReflectionUtil

class ReflectionTest {
    private fun test1() {

    }

    private fun test2() {

    }

    private fun test3() {

    }

    @Test
    fun checkPrivate() {
        Assert.assertEquals(3,
            ReflectionUtil.filterMethods(this, this.javaClass, ReflectionUtil.ReflectionMethodFilter.ACCESSOR_PRIVATE).size)
    }
}