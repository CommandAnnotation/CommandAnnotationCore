package skywolf46.commandannotationmc.minecraft.annotations

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

annotation class AutoComplete(vararg val command: String) {

}