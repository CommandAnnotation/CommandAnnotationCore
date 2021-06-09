package skywolf46.commandannotationmc.minecraft.impl.minecraft

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import skywolf46.commandannotation.kotlin.data.Arguments
import skywolf46.commandannotationmc.minecraft.CommandAnnotation
import skywolf46.extrautility.data.ArgumentStorage

class MinecraftCommandImpl : Command("") {
    override fun execute(p0: CommandSender, p1: String, p2: Array<String>): Boolean {
        val arguments = Arguments(false, "/$p1", ArgumentStorage(), p2, 0)
        arguments.addParameter(p0)
        arguments.addParameter(arguments._storage)
        arguments.addParameter(arguments)
        CommandAnnotation.command.inspect("/$p1", arguments)?.invoke(arguments)
        return true
    }
}