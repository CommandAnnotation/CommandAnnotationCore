package skywolf46.commandannotationmc.minecraft.registry.versionSpecific

import com.mojang.brigadier.Command
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.context.CommandContextBuilder
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import net.minecraft.server.v1_16_R3.CommandListenerWrapper
import net.minecraft.server.v1_16_R3.MinecraftServer
import java.util.concurrent.CompletableFuture

class SuggestionRegistry {
    companion object {
        fun emptyMethod() {

        }
    }

    init {
        MinecraftServer.getServer().commandDispatcher.a().root.addChild(RemappedCommandLiteral("test"))
        MinecraftServer.getServer().commandDispatcher.a()
            .register(LiteralArgumentBuilder.literal<CommandListenerWrapper>("test4"))
        println("Child added")

    }

    // Ignore minecraft logic, support dynamic parameter
    class RemappedCommandLiteral(val command: String) :
        LiteralCommandNode<CommandListenerWrapper>(command,
            Command { context ->
                println(context.input)
                Command.SINGLE_SUCCESS
            },
            { _ -> true }, null, null, false) {
        override fun parse(reader: StringReader?, contextBuilder: CommandContextBuilder<CommandListenerWrapper>?) {
//            super.parse(reader, contextBuilder)
        }

        override fun listSuggestions(
            context: CommandContext<CommandListenerWrapper>?,
            builder: SuggestionsBuilder?,
        ): CompletableFuture<Suggestions> {
            return Suggestions.empty()
        }

    }
}