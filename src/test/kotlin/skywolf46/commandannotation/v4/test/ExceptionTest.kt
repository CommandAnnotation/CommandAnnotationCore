package skywolf46.commandannotation.v4.test

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.commandannotation.v4.api.exceptions.CommandFailedException
import skywolf46.commandannotation.v4.api.util.CommandConditionUtil.and
import skywolf46.commandannotation.v4.api.util.CommandUtil
import skywolf46.commandannotation.v4.api.util.RequirementUtil.fail
import skywolf46.commandannotation.v4.api.util.RequirementUtil.intercept
import skywolf46.commandannotation.v4.api.util.RequirementUtil.maxLength
import skywolf46.commandannotation.v4.api.util.RequirementUtil.minLength
import skywolf46.commandannotation.v4.test.exceptions.SecondTestSucceedException
import skywolf46.commandannotation.v4.test.exceptions.TestSucceedException
import skywolf46.extrautility.core.data.ArgumentStorage

class ExceptionTest {
    @Test
    fun handleSingleException() {
        Assertions.assertThrows(TestSucceedException::class.java) {
            val args = Arguments(arrayOf("test1", "test2"), ArgumentStorage())
            with(args) {
                requires {
                    minLength(3) and maxLength(10) fail {
                        throw TestSucceedException()
                    }
                    fail {
                        throw IllegalStateException("Failed : Global fail-check triggered")
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
                    minLength(3) and maxLength(10)
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
                    minLength(3) and maxLength(10)
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
                    minLength(3) and maxLength(10) fail {
                        println("First fail")
                    }
                    minLength(5) fail {
                        throw IllegalStateException("Failed : Second fail-check triggered")
                    }
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

    @Test
    fun checkFailedConditionIntercept() {
        Assertions.assertThrows(CommandFailedException::class.java) {
            val args = Arguments(arrayOf("test1", "test2"), ArgumentStorage())
            with(args) {
                requires {
                    (minLength(3) and maxLength(10)) intercept {
                        println("First fail-check interceptor triggered")
                    }
                    minLength(5) fail {
                        throw IllegalStateException("Failed : Second fail-check triggered")
                    }

                    fail {
                        throw IllegalStateException("Failed : Global fail-check triggered")
                    }
                }
                throw IllegalStateException("Failed : Command executed")
            }
        }
    }

    @Test
    fun checkFailedConditionIntercept2() {
        Assertions.assertThrows(TestSucceedException::class.java) {
            val args = Arguments(arrayOf("test1", "test2"), ArgumentStorage())
            with(args) {
                requires {
                    minLength(5) fail {
                        println("First fail-check triggered")
                    }

                    minLength(3) and maxLength(10) intercept {
                        throw IllegalStateException("Second fail-check interceptor triggered")
                    }

                    minLength(3) fail {
                        throw IllegalStateException("Failed : Third fail-check triggered")
                    }

                    fail {
                        throw TestSucceedException()
                    }
                }
                throw IllegalStateException("Failed : Command executed")
            }
        }
    }

    @Test
    fun checkFailedConditionIntercept3() {
        Assertions.assertThrows(CommandFailedException::class.java) {
            val args = Arguments(arrayOf("test1", "test2"), ArgumentStorage())
            with(args) {
                requires {
                    minLength(1) fail {
                        throw IllegalStateException("First fail-check triggered")
                    }

                    minLength(3) and maxLength(10) intercept {
                        println("Second fail-check interceptor triggered")
                    }

                    minLength(3) fail {
                        throw IllegalStateException("Failed : Third fail-check triggered")
                    }

                    fail {
                        throw IllegalStateException("Failed : Global fail-check triggered")
                    }
                }
                throw IllegalStateException("Failed : Command executed")
            }
        }
    }

    @Test
    fun checkExpectedException() {
        Assertions.assertThrows(SecondTestSucceedException::class.java) {
            val args = Arguments(arrayOf("test1", "test2"), ArgumentStorage())
            CommandUtil.triggerCommand(args) {
                handle<TestSucceedException> {
                    throw SecondTestSucceedException()
                }
                throw TestSucceedException()
            }
        }
    }

    @Test
    fun checkUnexpectedExceptionTest() {
        Assertions.assertThrows(TestSucceedException::class.java) {
            val args = Arguments(arrayOf("test1", "test2"), ArgumentStorage())
            CommandUtil.triggerCommand(args) {
                throw TestSucceedException()
            }
        }
    }
}
