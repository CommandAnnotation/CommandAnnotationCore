package skywolf46.commandannotationmc.minecraft.registry

import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import skywolf46.commandannotation.kotlin.data.Arguments
import skywolf46.extrautility.util.connectedPlayerOf
import skywolf46.extrautility.util.playerOf

object MinecraftArgumentRegistry {
    fun register() {
        Arguments.register(OfflinePlayer::class) {
            connectedPlayerOf(next()) ?: throw NullPointerException()
        }


        Arguments.register(Player::class) {
            playerOf(next()) ?: throw NullPointerException()
        }
    }


}
