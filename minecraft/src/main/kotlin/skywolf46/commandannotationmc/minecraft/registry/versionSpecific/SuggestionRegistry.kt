package skywolf46.commandannotationmc.minecraft.registry.versionSpecific

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.server.v1_16_R3.CommandListenerWrapper
import net.minecraft.server.v1_16_R3.MinecraftServer
import java.util.concurrent.CompletableFuture

class SuggestionRegistry {
    companion object {
        fun emptyMethod() {

        }
    }

    init {
        RemappedCommandLiteral("Test", true).register()
        MinecraftServer.getServer().commandDispatcher.a()
            .register(LiteralArgumentBuilder.literal<CommandListenerWrapper>("test4"))
        println("Child added")
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


        override fun run(p0: CommandContext<CommandListenerWrapper>?): Int {
            TODO("Not yet implemented")
        }

        override fun getSuggestions(
            p0: CommandContext<CommandListenerWrapper>?,
            p1: SuggestionsBuilder?,
        ): CompletableFuture<Suggestions> {
            TODO("Not yet implemented")
        }

    }
}