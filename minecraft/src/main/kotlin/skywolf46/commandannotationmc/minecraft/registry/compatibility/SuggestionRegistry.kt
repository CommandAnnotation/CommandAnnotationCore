package skywolf46.commandannotationmc.minecraft.registry.compatibility

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.context.StringRange
import com.mojang.brigadier.suggestion.Suggestion
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import org.bukkit.Bukkit
import org.bukkit.Server
import skywolf46.commandannotation.kotlin.data.Arguments
import skywolf46.commandannotationmc.minecraft.CommandAnnotation
import skywolf46.extrautility.data.ArgumentStorage
import skywolf46.extrautility.util.extractField
import skywolf46.extrautility.util.invokeMethod
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicInteger

object SuggestionRegistry {
    private val literals = mutableMapOf<String, AtomicInteger>()


    fun getVersion(server: Server): String {
        val packageName = server.javaClass.getPackage().name
        return packageName.substring(packageName.lastIndexOf('.') + 1)
    }


    fun getNMSClass(className: String): Class<*> {
        return try {
            Class.forName("net.minecraft.server." + getVersion(Bukkit.getServer()) + "." + className)
        } catch (ex: Exception) {
            Void.TYPE
        }
    }


    fun getOBCClass(className: String): Class<*> {
        return Class.forName("org.bukkit.craftbukkit." + getVersion(Bukkit.getServer()) + "." + className)
    }

    fun unregister(start: String) {
        if (literals.getOrElse(start) { AtomicInteger(0) }.decrementAndGet() <= 0) {
            literals.remove(start)
            Bukkit.getServer().invokeMethod("getServer")!!
                .invokeMethod("getCommandDispatcher")!!
                .invokeMethod("a")!!
                .extractField<Any>("root")!!
                .invokeMethod("removeCommand", start)
        }
    }

    // Ignore minecraft logic, support dynamic parameter
    class RemappedCommandLiteral(val command: String) :
        Command<Any>, SuggestionProvider<Any> {

        // To prevent multiple command register
        fun register(): RemappedCommandLiteral {
            literals.computeIfAbsent(command) {
                AtomicInteger(0)
            }.apply {
                if (get() == 0) {
                    forceRegister()
                }
                incrementAndGet()
            }
            return this
        }

        fun forceRegister() {
            getNMSClass("MinecraftServer").getMethod("getServer").invoke(null)
                .invokeMethod("getCommandDispatcher")!!
                .invokeMethod("a")!!
                .invokeMethod("register", LiteralArgumentBuilder.literal<Any>(command).executes(this).then(
                    RequiredArgumentBuilder.argument<Any, String>("args",
                        StringArgumentType.greedyString()).suggests(this)
                        .executes(this)))
        }

        override fun run(p0: CommandContext<Any>): Int {
            return Command.SINGLE_SUCCESS
        }

        override fun getSuggestions(
            p0: CommandContext<Any>,
            p1: SuggestionsBuilder,
        ): CompletableFuture<Suggestions> {
            val args = Arguments(false, ArgumentStorage(), p0.input)
            val suggestions = mutableListOf<Suggestion>()
            val range = StringRange(p1.input.length, p1.input.length)
            val inspected = CommandAnnotation.command.inspectNextCondition(args.command, args)
            val last = args.last()
            for (x in inspected) {
                for (str in x.findNextAutoComplete(args.clone(), false)) {
                    if (args.last().isEmpty() || (str != args.last() && str.startsWith(args.last())))
                        suggestions.add(Suggestion(StringRange(p1.input.length, p1.input.length),
                            str
                        ) { "CA generated arguments" })
                }
            }
            val suggestion =
                Suggestions(StringRange(p1.input.length - last.length, p1.input.length), suggestions)
            return CompletableFuture.completedFuture(suggestion)
        }

        fun unregister() {

        }

    }
}