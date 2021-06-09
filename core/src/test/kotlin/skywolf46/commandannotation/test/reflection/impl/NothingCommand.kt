package skywolf46.commandannotation.test.reflection.impl

import com.sun.xml.internal.fastinfoset.util.StringArray
import skywolf46.commandannotation.kotlin.abstraction.ICommand
import skywolf46.extrautility.data.ArgumentStorage

class NothingCommand : ICommand {
    override fun invoke(storage: ArgumentStorage) {
        println("Hello, World!")
    }

    override fun onUnregister(commandStart: StringArray) {
        TODO("Not yet implemented")
    }

    override fun onRegister(commandStart: StringArray) {
        TODO("Not yet implemented")
    }
}