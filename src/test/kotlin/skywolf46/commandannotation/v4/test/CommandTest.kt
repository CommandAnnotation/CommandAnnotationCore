package skywolf46.commandannotation.v4.test

import org.junit.jupiter.api.Test
import skywolf46.commandannotation.v4.data.Arguments
import skywolf46.commandannotation.v4.util.CommandConditionUtil.and
import skywolf46.commandannotation.v4.util.CommandConditionUtil.ifFail
import skywolf46.commandannotation.v4.util.RequirementUtil.maxLength
import skywolf46.commandannotation.v4.util.RequirementUtil.minLength
import skywolf46.extrautility.data.ArgumentStorage

class CommandTest {
    @Test
    fun failBackTest() {
        val args = Arguments(arrayOf("test1", "test2"), ArgumentStorage())
        args.requires {
            minLength(10) ifFail {
                println("MinLength check failed")
            } maxLength(10) ifFail {
                println("MaxLength check failed")
            }
        }
        println("Hey, that's pretty good!")
    }
}