package skywolf46.commandannotation.v4.test.data

import skywolf46.commandannotation.v4.api.data.Arguments

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
}