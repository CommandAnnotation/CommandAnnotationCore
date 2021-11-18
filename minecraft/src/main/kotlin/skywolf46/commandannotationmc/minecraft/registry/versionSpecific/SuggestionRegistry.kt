package skywolf46.commandannotationmc.minecraft.registry.versionSpecific

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
import skywolf46.commandannotation.kotlin.annotation.CompactCondition
import skywolf46.commandannotation.kotlin.data.Arguments
import skywolf46.commandannotationmc.minecraft.CommandAnnotation
import skywolf46.commandannotationmc.minecraft.annotations.MinecraftCommand
import skywolf46.extrautility.data.ArgumentStorage
import java.util.concurrent.CompletableFuture

object SuggestionRegistry {

    init {
        RemappedCommandLiteral("test", true).register()
        MinecraftServer.getServer().commandDispatcher.a()
            .register(LiteralArgumentBuilder.literal<CommandListenerWrapper>("test4"))
        println("Child added")
    }

    @MinecraftCommand("/test a <test>")
    fun test() {

    }

    @MinecraftCommand("/test test2")
    fun test1() {

    }

    @MinecraftCommand("/test test3")
    fun test2() {

    }

    @MinecraftCommand("/test test4")
    fun test3() {

    }

    @MinecraftCommand("/test test5")
    fun test4() {

    }

    @CompactCondition("test")
    fun onCompactTestComplete(): Array<String> {
        return arrayOf("test1", "test59", "asde")
    }

    // Ignore minecraft logic, support dynamic parameter
    class RemappedCommandLiteral(val command: String, val doRandomNode: Boolean) :
        Command<CommandListenerWrapper>, SuggestionProvider<CommandListenerWrapper> {

        fun register() {
            MinecraftServer.getServer().commandDispatcher.a()
                .register(LiteralArgumentBuilder.literal<CommandListenerWrapper?>(command).executes(this).then(
                    RequiredArgumentBuilder.argument<CommandListenerWrapper, String>("args",
                        StringArgumentType.greedyString()).suggests(this)
                        .executes(this)))
        }


        override fun run(p0: CommandContext<CommandListenerWrapper>): Int {
            println(p0.input)
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
            println("Inspected: ${inspected}")
            println(inspected.size)
            println("'${p1.input}'")
            val last = args.last()
            for (x in inspected) {
                println(x)
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

    }
}