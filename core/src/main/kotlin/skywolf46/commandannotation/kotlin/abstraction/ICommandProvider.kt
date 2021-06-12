package skywolf46.commandannotation.kotlin.abstraction

import skywolf46.extrautility.util.MethodInvoker

interface ICommandProvider<T : Annotation> {
    fun generateCommand(annotation: T, wrapper: MethodInvoker): ICommand

}