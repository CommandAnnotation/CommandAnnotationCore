package skywolf46.commandannotation.v4.api.util

import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.commandannotation.v4.api.exceptions.CommandFailedException

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
}