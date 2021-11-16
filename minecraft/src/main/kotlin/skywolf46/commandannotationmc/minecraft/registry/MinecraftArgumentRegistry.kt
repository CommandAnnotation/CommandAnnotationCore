package skywolf46.commandannotationmc.minecraft.registry

import org.bukkit.OfflinePlayer
import skywolf46.commandannotation.kotlin.data.Arguments
import skywolf46.extrautility.util.connectedPlayerOf

object MinecraftArgumentRegistry {
    fun register() {
        Arguments.register(OfflinePlayer::class) {
            connectedPlayerOf(next()) ?: throw NullPointerException()
        }
      }


}
