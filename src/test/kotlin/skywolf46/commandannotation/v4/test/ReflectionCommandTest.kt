package skywolf46.commandannotation.v4.test

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import skywolf46.commandannotation.v4.CommandAnnotationCore
import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.commandannotation.v4.initializer.CommandCore
import skywolf46.commandannotation.v4.test.data.TestCommandAnnotation
import skywolf46.commandannotation.v4.test.exceptions.TestSucceedException
import skywolf46.extrautility.data.ArgumentStorage

@TestMethodOrder(OrderAnnotation::class)
class ReflectionCommandTest {

    @Test
    @Order(0)
    fun testInitialization() {
        CommandAnnotationCore.init()
        println("Test")
    }


    @Test
    fun testCommandFind() {
        Assertions.assertNotNull(
            CommandCore.find(
                TestCommandAnnotation::class,
                Arguments(arrayOf("/test1234"), ArgumentStorage())
            )
        )
    }


    @Test
    fun testCommandFind02() {
        Assertions.assertNotNull(
            CommandCore.find(
                TestCommandAnnotation::class,
                Arguments("/test1234 asdf".split(" ").toTypedArray(), ArgumentStorage())
            )
        )

        Assertions.assertNotNull(
            CommandCore.find(
                TestCommandAnnotation::class,
                Arguments("/test1234".split(" ").toTypedArray(), ArgumentStorage())
            )
        )

        Assertions.assertNull(
            CommandCore.find(
                TestCommandAnnotation::class,
                Arguments("/test12345 asdf2".split(" ").toTypedArray(), ArgumentStorage())
            )
        )
    }

    @Test
    fun testCommandFind03() {
        Assertions.assertNotNull(
            CommandCore.find(
                TestCommandAnnotation::class,
                Arguments("/test1234 asdf asdf".split(" ").toTypedArray(), ArgumentStorage())
            )
        )


        Assertions.assertNotNull(
            CommandCore.find(
                TestCommandAnnotation::class,
                Arguments("/test1234 asdf fdsa".split(" ").toTypedArray(), ArgumentStorage())
            )
        )

        Assertions.assertNotNull(
            CommandCore.find(
                TestCommandAnnotation::class,
                Arguments("/test1234 asdf 2fdsa".split(" ").toTypedArray(), ArgumentStorage())
            )
        )
    }

    @Test
    fun parameterizedCommandTest() {
        Assertions.assertThrows(TestSucceedException::class.java) {
            Arguments("/test1234 parameter test_01".split(" ").toTypedArray(), ArgumentStorage()).apply {
                CommandCore.find(
                    TestCommandAnnotation::class,
                    this
                )!!.invokeCommand(this)
            }
        }.apply {
            Assertions.assertEquals("Test01", this.msg)
        }


        Assertions.assertThrows(TestSucceedException::class.java) {
            Arguments("/test1234 test_02 parameter".split(" ").toTypedArray(), ArgumentStorage()).apply {
                CommandCore.find(
                    TestCommandAnnotation::class,
                    this
                )!!.invokeCommand(this)
            }
        }.apply {
            Assertions.assertEquals("Test02", this.msg)
        }


        Assertions.assertThrows(TestSucceedException::class.java) {
            Arguments("/test1234 test_03".split(" ").toTypedArray(), ArgumentStorage()).apply {
                CommandCore.find(
                    TestCommandAnnotation::class,
                    this
                )!!.invokeCommand(this)
            }
        }.apply {
            Assertions.assertEquals("TestFallback", this.msg)
        }
    }
}