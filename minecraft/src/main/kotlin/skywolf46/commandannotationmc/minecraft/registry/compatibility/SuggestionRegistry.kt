package skywolf46.commandannotationmc.minecraft.registry.compatibility

import com.mojang.brigadier.Command
import com.mojang.brigadier.Message
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.context.StringRange
import com.mojang.brigadier.suggestion.Suggestion
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.server.v1_16_R3.CommandListenerWrapper
import net.minecraft.server.v1_16_R3.MinecraftServer
import skywolf46.commandannotation.kotlin.data.Arguments
import skywolf46.commandannotationmc.minecraft.CommandAnnotation
import skywolf46.extrautility.data.ArgumentStorage
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicInteger

object SuggestionRegistry {
    private val literals = mutableMapOf<String, AtomicInteger>()

    fun unregister(start: String) {
        if (literals.getOrElse(start) { AtomicInteger(0) }.decrementAndGet() <= 0) {
            literals.remove(start)
            MinecraftServer.getServer().commandDispatcher.a().root.removeCommand(start)
        }
    }

    // Ignore minecraft logic, support dynamic parameter
    class RemappedCommandLiteral(val command: String) :
        Command<CommandListenerWrapper>, SuggestionProvider<CommandListenerWrapper> {

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

        // TODO Change to reflection
        fun forceRegister() {
            MinecraftServer.getServer().commandDispatcher.a()
                .register(LiteralArgumentBuilder.literal<CommandListenerWrapper?>(command).executes(this).then(
                    RequiredArgumentBuilder.argument<CommandListenerWrapper, String>("args",
                        StringArgumentType.greedyString()).suggests(this)
                        .executes(this)))
        }

        override fun run(p0: CommandContext<CommandListenerWrapper>): Int {
            return Command.SINGLE_SUCCESS
        }

        override fun getSuggestions(
            p0: CommandContext<CommandListenerWrapper>,
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
                            str,
                            Message { "CA generated arguments" }))
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