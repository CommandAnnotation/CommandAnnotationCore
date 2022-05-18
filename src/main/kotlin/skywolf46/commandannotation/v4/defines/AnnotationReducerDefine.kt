package skywolf46.commandannotation.v4.defines

import skywolf46.commandannotation.v4.api.annotations.CommandContainer
import skywolf46.commandannotation.v4.api.annotations.define.AnnotationConverter
import skywolf46.commandannotation.v4.api.annotations.define.AnnotationReducer
import skywolf46.commandannotation.v4.data.CommandContainerWrapper

object AnnotationReducerDefine {
    @AnnotationReducer
    fun reduceCommand(first: CommandContainerWrapper, second: CommandContainerWrapper): CommandContainerWrapper {
        val cmdList = mutableListOf<String>()
        first.getCommand().forEach { cmdFirst ->
            second.getCommand().forEach { cmdSecond ->
                cmdList.add("$cmdFirst $$cmdSecond")
            }
        }
        return CommandContainerWrapper(cmdList)
    }


    @AnnotationConverter
    fun convertCommand(annotation: CommandContainer): CommandContainerWrapper {
        return CommandContainerWrapper(annotation.alias.toMutableList().apply {
            add(annotation.command)
        })
    }

}