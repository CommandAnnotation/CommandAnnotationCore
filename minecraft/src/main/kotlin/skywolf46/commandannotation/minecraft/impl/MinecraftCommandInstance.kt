package skywolf46.commandannotation.minecraft.impl

import skywolf46.commandannotation.kotlin.abstraction.ICommand
import skywolf46.commandannotation.kotlin.data.CommandWrapper
import skywolf46.extrautility.data.ArgumentStorage
import skywolf46.extrautility.util.MethodWrapper

class MinecraftCommandInstance(private val wrapper: CommandWrapper) : ICommand {
    lateinit var unregister: () -> Unit
    override fun getCommand(): Array<String> {
        TODO("Not yet implemented")
    }

    override fun invoke(storage: ArgumentStorage) {
        wrapper.invoke(storage)
    }

    override fun bindUnregisterLambda(unit: () -> Unit) {
        this.unregister = unit
    }

}