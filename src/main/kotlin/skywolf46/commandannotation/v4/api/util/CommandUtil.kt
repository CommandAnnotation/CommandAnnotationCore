package skywolf46.commandannotation.v4.api.util

import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.commandannotation.v4.api.exceptions.CommandFailedException
import skywolf46.commandannotation.v4.initializer.CommandCore
import skywolf46.commandannotation.v4.initializer.CommandGeneratorCore
import skywolf46.extrautility.core.data.ArgumentStorage
import kotlin.reflect.KClass

object CommandUtil {
    fun triggerCommand(args: Arguments, unit: Arguments.() -> Unit) {
        try {
            unit(args)
        } catch (e: CommandFailedException) {
            // Do nothing, CommandFailedException is expected exception
            // Can override root CommandFailedException handler with [Arguments#expect]
        } catch (e: Throwable) {
            args.rootHandler.throwIfUnexpected(e)
        }
    }

    fun triggerCommand(type: KClass<out Annotation>, str: String, args: ArgumentStorage) : Boolean {
        val argument = CommandGeneratorCore.generateArgument(type, args, str)
        CommandCore.find(type, argument!!)?.invokeCommand(argument) ?: return false
        return true
    }
}