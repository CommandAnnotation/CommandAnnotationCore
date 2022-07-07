package skywolf46.commandannotation.v4.test

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.commandannotation.v4.api.exceptions.CommandFailedException
import skywolf46.commandannotation.v4.api.util.CommandConditionUtil.and
import skywolf46.commandannotation.v4.api.util.CommandConditionUtil.or
import skywolf46.commandannotation.v4.api.util.RequirementUtil.length
import skywolf46.commandannotation.v4.api.util.RequirementUtil.maxLength
import skywolf46.commandannotation.v4.api.util.RequirementUtil.minLength
import skywolf46.extrautility.core.data.ArgumentStorage

class CommandTest {
    @Test
    fun failBackTest() {
        Assertions.assertThrows(CommandFailedException::class.java) {
            val args = Arguments(arrayOf("test1", "test2"), ArgumentStorage())
            with(args) {
                requires {
                    (minLength(3) and maxLength(10)) or length(1)
                    fail {
                        println("Global fail check")
                    }
                }
                println("Hey, that's pretty good!")
            }
        }
    }

    @Test
    fun successTest() {
        val args = Arguments(arrayOf("test1", "test2", "test3"), ArgumentStorage())
        args.requires {
            (minLength(3) and maxLength(10)) or length(1)
            fail {
                println("Global fail check")
            }
        }
        println("Hey, that's pretty good!")
    }
}