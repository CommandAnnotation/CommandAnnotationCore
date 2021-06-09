package skywolf46.commandannotation.test.reflection

import org.junit.Test
import skywolf46.commandannotation.kotlin.data.CommandStorage
import skywolf46.commandannotation.kotlin.util.CommandInspector
import skywolf46.commandannotation.test.reflection.impl.NothingCommand
import skywolf46.extrautility.data.ArgumentStorage

class CommandTest {
    @Test
    fun test() {
        val inspected = CommandInspector.inspect("/test <int> <string> test41, <int> test5")
        inspected.contentToString().apply {
            println(this)
        }
        val storage = CommandStorage<NothingCommand>()
        storage.registerCommand(NothingCommand(), *inspected)
        storage.inspectCommand("/test 20 test test41, 20 test5", ArgumentStorage())!!.invoke(ArgumentStorage())
    }
}