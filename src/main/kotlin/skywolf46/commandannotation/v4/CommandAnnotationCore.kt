package skywolf46.commandannotation.v4

import skywolf46.commandannotation.v4.api.signal.trigger.SerializerRegisterSignal
import skywolf46.commandannotation.v4.api.util.callSignal
import skywolf46.commandannotation.v4.initializer.CommandCore
import skywolf46.commandannotation.v4.initializer.CommandGeneratorCore
import skywolf46.commandannotation.v4.initializer.Deserializers
import skywolf46.commandannotation.v4.initializer.SignalCore

object CommandAnnotationCore {

    fun init() {
        println("CommandAnnotation-Core | Initializing..")

        println("CommandAnnotation-Core | Registering default serializers..")
        Deserializers.init()

        println("CommandAnnotation-Core | Initializing signal core..")
        SignalCore.init()

        println("CommandAnnotation-Core | Calling serializer signal..")
        SerializerRegisterSignal().callSignal()

        println("CommandAnnotation-Core | Initializing command generator core..")
        CommandGeneratorCore.init()

        println("CommandAnnotation-Core | Initializing command core..")
        CommandCore.init()

    }


}