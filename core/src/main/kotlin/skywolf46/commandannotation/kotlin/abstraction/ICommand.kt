package skywolf46.commandannotation.kotlin.abstraction

import com.sun.xml.internal.fastinfoset.util.StringArray
import skywolf46.commandannotation.kotlin.data.Arguments
import skywolf46.commandannotation.kotlin.util.CommandInspector
import skywolf46.extrautility.data.ArgumentStorage
import java.util.*

interface ICommand {
    fun getRawCommand(): Array<out String>

    fun invoke(storage: Arguments)

    fun onUnregister(commandStart: String, vararg condition: ICommandCondition)

    fun onRegister(commandStart: String, vararg condition: ICommandCondition)

    fun onRegister() {
        for (cmd in getRawCommand()) {
            val cmdSplit = cmd.trim().split(" ")
            val base = cmdSplit[0]
            val condition = CommandInspector.inspect(cmd.trim().substring(base.length + 1).trim())
            onRegister(base, *condition)
        }
    }

    fun onUnregister() {
        for (cmd in getRawCommand()) {
            val cmdSplit = cmd.trim().split(" ")
            val base = cmdSplit[0]
            val condition = CommandInspector.inspect(cmd.trim().substring(base.length + 1).trim())
            onUnregister(base, *condition)
        }
    }

}