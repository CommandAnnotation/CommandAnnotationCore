package skywolf46.commandannotationmc.minecraft.util

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.SimpleCommandMap
import org.bukkit.help.HelpMap
import java.lang.reflect.Field


object MinecraftCommandExtractor {
    @Deprecated("")
    fun parseCommandMap(): HashMap<String, Command>? {
        try {
            val cl: Class<*> = SimpleCommandMap::class.java
            val fl: Field = cl.getDeclaredField("knownCommands")
            fl.setAccessible(true)
            val cx: Class<*> = Bukkit.getServer().javaClass
            val target = cx.getMethod("getCommandMap").invoke(Bukkit.getServer())
            return fl.get(target) as HashMap<String, Command>
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }


    @Deprecated("")
    fun parseHelpMap(): HelpMap? {
        try {
            val cx: Class<*> = Bukkit.getServer().javaClass
            val target = cx.getMethod("getHelpMap").invoke(Bukkit.getServer())
            return target as HelpMap
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }
}