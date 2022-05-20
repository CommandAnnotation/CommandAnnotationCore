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


    @Test
    fun commandArgumentStringParsingTest() {
        Assertions.assertThrows(TestSucceedException::class.java) {
            Arguments("/test1234 stringTest test!".split(" ").toTypedArray(), ArgumentStorage()).apply {
                CommandCore.find(
                    TestCommandAnnotation::class,
                    this
                )!!.invokeCommand(this)
            }
        }

        Assertions.assertThrows(TestSucceedException::class.java) {
            Arguments("/test1234 stringTest2 Test1 Test2".split(" ").toTypedArray(), ArgumentStorage()).apply {
                CommandCore.find(
                    TestCommandAnnotation::class,
                    this
                )!!.invokeCommand(this)
            }
        }
    }

    @Test
    fun commandArgumentByteParsingTest() {
        Assertions.assertThrows(TestSucceedException::class.java) {
            Arguments("/test1234 byteTest 11".split(" ").toTypedArray(), ArgumentStorage()).apply {
                CommandCore.find(
                    TestCommandAnnotation::class,
                    this
                )!!.invokeCommand(this)
            }
        }

        Assertions.assertThrows(TestSucceedException::class.java) {
            Arguments("/test1234 byteTest2 24 60".split(" ").toTypedArray(), ArgumentStorage()).apply {
                CommandCore.find(
                    TestCommandAnnotation::class,
                    this
                )!!.invokeCommand(this)
            }
        }
    }

    @Test
    fun commandArgumentShortParsingTest() {
        Assertions.assertThrows(TestSucceedException::class.java) {
            Arguments("/test1234 shortTest 1187".split(" ").toTypedArray(), ArgumentStorage()).apply {
                CommandCore.find(
                    TestCommandAnnotation::class,
                    this
                )!!.invokeCommand(this)
            }
        }

        Assertions.assertThrows(TestSucceedException::class.java) {
            Arguments("/test1234 shortTest2 9921 9378".split(" ").toTypedArray(), ArgumentStorage()).apply {
                CommandCore.find(
                    TestCommandAnnotation::class,
                    this
                )!!.invokeCommand(this)
            }
        }
    }

    @Test
    fun commandArgumentIntParsingTest() {
        Assertions.assertThrows(TestSucceedException::class.java) {
            Arguments("/test1234 intTest 49237".split(" ").toTypedArray(), ArgumentStorage()).apply {
                CommandCore.find(
                    TestCommandAnnotation::class,
                    this
                )!!.invokeCommand(this)
            }
        }

        Assertions.assertThrows(TestSucceedException::class.java) {
            Arguments("/test1234 intTest2 97867 28991".split(" ").toTypedArray(), ArgumentStorage()).apply {
                CommandCore.find(
                    TestCommandAnnotation::class,
                    this
                )!!.invokeCommand(this)
            }
        }
    }

    @Test
    fun commandArgumentFloatParsingTest() {
        Assertions.assertThrows(TestSucceedException::class.java) {
            Arguments("/test1234 floatTest 21390".split(" ").toTypedArray(), ArgumentStorage()).apply {
                CommandCore.find(
                    TestCommandAnnotation::class,
                    this
                )!!.invokeCommand(this)
            }
        }

        Assertions.assertThrows(TestSucceedException::class.java) {
            Arguments("/test1234 floatTest2 11978 52911".split(" ").toTypedArray(), ArgumentStorage()).apply {
                CommandCore.find(
                    TestCommandAnnotation::class,
                    this
                )!!.invokeCommand(this)
            }
        }
    }

    @Test
    fun commandArgumentDoubleParsingTest() {
        Assertions.assertThrows(TestSucceedException::class.java) {
            Arguments("/test1234 doubleTest 26269.3".split(" ").toTypedArray(), ArgumentStorage()).apply {
                CommandCore.find(
                    TestCommandAnnotation::class,
                    this
                )!!.invokeCommand(this)
            }
        }

        Assertions.assertThrows(TestSucceedException::class.java) {
            Arguments("/test1234 doubleTest2 18344.7 88287.4".split(" ").toTypedArray(), ArgumentStorage()).apply {
                CommandCore.find(
                    TestCommandAnnotation::class,
                    this
                )!!.invokeCommand(this)
            }
        }
    }

    @Test
    fun commandArgumentLongParsingTest() {
        Assertions.assertThrows(TestSucceedException::class.java) {
            Arguments("/test1234 longTest 52519".split(" ").toTypedArray(), ArgumentStorage()).apply {
                CommandCore.find(
                    TestCommandAnnotation::class,
                    this
                )!!.invokeCommand(this)
            }
        }

        Assertions.assertThrows(TestSucceedException::class.java) {
            Arguments("/test1234 longTest2 87816 73467".split(" ").toTypedArray(), ArgumentStorage()).apply {
                CommandCore.find(
                    TestCommandAnnotation::class,
                    this
                )!!.invokeCommand(this)
            }
        }
    }
}