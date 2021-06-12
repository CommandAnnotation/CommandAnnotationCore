package skywolf46.commandannotation.kotlin.data

import skywolf46.commandannotation.kotlin.abstraction.AbstractAnnotable
import skywolf46.commandannotation.kotlin.annotation.Mark
import skywolf46.extrautility.util.MethodInvoker

class MarkedMethod(wrapper: MethodInvoker) :
    AbstractAnnotable(wrapper, wrapper.method.getAnnotation(Mark::class.java)!!.markPriority) {

}