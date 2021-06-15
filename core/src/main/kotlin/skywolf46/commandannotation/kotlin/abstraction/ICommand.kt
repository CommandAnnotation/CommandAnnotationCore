package skywolf46.commandannotation.kotlin.abstraction

import skywolf46.commandannotation.kotlin.data.Arguments
import skywolf46.commandannotation.kotlin.util.CommandInspector
import skywolf46.extrautility.util.MethodInvoker

interface ICommand {
    fun getMethod(): MethodInvoker

    fun getRawCommand(): Array<out String>

    fun invoke(storage: Arguments) : Boolean

    fun onUnregister(commandStart: String, vararg condition: ICommandCondition)

    fun onRegister(commandStart: String, vararg condition: ICommandCondition)

    fun onRegister() {
        for (cmd in getRawCommand()) {
            println("CommandAnnotation-Core | ..Registered command ${getMethod().method.declaringClass.kotlin.qualifiedName}#${getMethod().method.name} as \"${cmd}\"")
            val cmdSplit = cmd.trim().split(" ")
            val base = cmdSplit[0]
            val condition =
                if (cmdSplit.size == 1) emptyArray() else CommandInspector.inspect(cmd.trim().substring(base.length + 1)
                    .trim())
            onRegister(base, *condition)
        }
    }

    fun onUnregister() {
        for (cmd in getRawCommand()) {
            val cmdSplit = cmd.trim().split(" ")
            val base = cmdSplit[0]
            val condition =
                if (cmdSplit.size == 1) emptyArray() else CommandInspector.inspect(cmd.trim().substring(base.length + 1)
                    .trim())
            onUnregister(base, *condition)
        }
    }

}