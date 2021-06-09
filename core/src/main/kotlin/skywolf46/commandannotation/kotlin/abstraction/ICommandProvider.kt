package skywolf46.commandannotation.kotlin.abstraction

import com.sun.xml.internal.fastinfoset.util.StringArray
import skywolf46.extrautility.util.MethodInvoker
import skywolf46.extrautility.util.MethodWrapper

interface ICommandProvider<T : Annotation> {
    fun generateCommand(annotation: T, wrapper: MethodInvoker): ICommand

}