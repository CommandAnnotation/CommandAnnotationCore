package skywolf46.commandannotation.kotlin.abstraction

import skywolf46.extrautility.util.MethodWrapper

interface ICommandProvider<T : Annotation> {
    fun generateCommand(annotation: T, wrapper: MethodWrapper): ICommand
}