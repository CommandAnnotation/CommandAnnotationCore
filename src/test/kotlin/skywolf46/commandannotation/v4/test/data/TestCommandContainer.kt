package skywolf46.commandannotation.v4.test.data

import skywolf46.commandannotation.v4.api.data.Arguments

object TestCommandContainer {
    @TestCommandAnnotation("/test1234")
    fun testCommand(args: Arguments) {

    }
}