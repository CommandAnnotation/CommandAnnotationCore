package skywolf46.commandannotation.v4.test.data

import org.junit.jupiter.api.Assertions
import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.commandannotation.v4.test.exceptions.TestSucceedException

object TestCommandContainer {
    @TestCommandAnnotation("/test1234")
    fun testCommand(args: Arguments) {

    }


    @TestCommandAnnotation("/test1234 asdf")
    fun testCommand2(args: Arguments) {

    }


    @TestCommandAnnotation("/test1234 asdf fdsa")
    fun testCommand3(args: Arguments) {

    }


    @TestCommandAnnotation("/test1234 asdf asdf")
    fun testCommand4(args: Arguments) {

    }

    @TestCommandAnnotation("/test1234 parameter <test>")
    fun parameterizedTestCommand01(args: Arguments) {
        Assertions.assertEquals("01", args.next())
        throw TestSucceedException("Test01")
    }


    @TestCommandAnnotation("/test1234 <test> parameter")
    fun parameterizedTestCommand02(args: Arguments) {
        Assertions.assertEquals("02", args.next())
        throw TestSucceedException("Test02")
    }


    @TestCommandAnnotation("/test1234 <test>")
    fun parameterizedFallbackTestCommand(args: Arguments) {
        throw TestSucceedException("TestFallback")
    }

}