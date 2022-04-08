package skywolf46.commandannotation.v4

import skywolf46.commandannotation.v4.api.signal.trigger.SerializerRegisterSignal
import skywolf46.commandannotation.v4.initializer.CommandCore
import skywolf46.commandannotation.v4.initializer.Serializers
import skywolf46.commandannotation.v4.initializer.SignalCore
import skywolf46.extrautility.util.triggerEvent

object CommandAnnotationCore {

    fun init() {
        println("CommandAnnotation-Core | Initializing..")

        println("CommandAnnotation-Core | Registering default serializers..")
        Serializers.init()

        println("CommandAnnotation-Core | Initializing signal core..")
        SignalCore.init()

        println("CommandAnnotation-Core | Calling serializer signal..")
        SerializerRegisterSignal().triggerEvent()

        println("CommandAnnotation-Core | Initializing command core..")
        CommandCore.init()

    }


}