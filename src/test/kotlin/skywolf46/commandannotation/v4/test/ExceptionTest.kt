package skywolf46.commandannotation.v4.test

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.commandannotation.v4.api.exceptions.CommandFailedException
import skywolf46.commandannotation.v4.api.util.CommandConditionUtil.and
import skywolf46.commandannotation.v4.api.util.RequirementUtil.fail
import skywolf46.commandannotation.v4.api.util.RequirementUtil.maxLength
import skywolf46.commandannotation.v4.api.util.RequirementUtil.minLength
import skywolf46.commandannotation.v4.test.exceptions.TestSucceedException
import skywolf46.extrautility.data.ArgumentStorage

class ExceptionTest {
    @Test
    fun handleSingleException() {
        Assertions.assertThrows(TestSucceedException::class.java) {
            val args = Arguments(arrayOf("test1", "test2"), ArgumentStorage())
            with(args) {
                requires {
                    (minLength(3) and maxLength(10)) fail {
                        throw TestSucceedException()
                    }
                    fail {
                        throw IllegalStateException("Failed : Caught on global exception handler")
                    }
                }
                throw IllegalStateException("Failed : Command executed")
            }
        }
    }

    @Test
    fun `handleSingleExceptionPass-through`() {
        Assertions.assertThrows(TestSucceedException::class.java) {
            val args = Arguments(arrayOf("test1", "test2"), ArgumentStorage())
            with(args) {
                requires {
                    (minLength(3) and maxLength(10))
                    fail {
                        throw TestSucceedException()
                    }
                }
                throw IllegalStateException("Failed : Command executed")
            }
        }
    }

    @Test
    fun checkUnhandledFailedCondition() {
        Assertions.assertThrows(CommandFailedException::class.java) {
            val args = Arguments(arrayOf("test1", "test2"), ArgumentStorage())
            with(args) {
                requires {
                    (minLength(3) and maxLength(10))
                }
                throw IllegalStateException("Failed : Command executed")
            }
        }
    }

    @Test
    fun checkMultipleConditionFailed() {
        Assertions.assertThrows(CommandFailedException::class.java) {
            val args = Arguments(arrayOf("test1", "test2"), ArgumentStorage())
            with(args) {
                requires {
                    (minLength(3) and maxLength(10)) fail {
                        println("First fail")
                    } and (minLength(5) fail {
                        throw IllegalStateException("Failed : Second condition fail trigger called")
                    })
                }
                throw TestSucceedException()
            }
        }
    }

    @Test
    fun checkAllFailCheckedCondition() {
        Assertions.assertThrows(TestSucceedException::class.java) {
            val args = Arguments(arrayOf("test1", "test2"), ArgumentStorage())
            with(args) {
                requires {
                    (minLength(3) and maxLength(10)) fail {
                        println("First fail-check triggered")
                    }
                    minLength(5) fail {
                        throw IllegalStateException("Failed : Second fail-check triggered")
                    }

                    fail {
                        throw TestSucceedException()
                    }
                }
                throw IllegalStateException("Failed : Command executed")
            }
        }
    }
}
