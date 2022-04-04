package skywolf46.commandannotation.v4

import skywolf46.commandannotation.v4.api.abstraction.IAnnotationConverter
import skywolf46.commandannotation.v4.api.signal.trigger.SerializerRegisterSignal
import skywolf46.commandannotation.v4.data.AnnotationConverterWrapper
import skywolf46.commandannotation.v4.data.AnnotationReducerWrapper
import skywolf46.commandannotation.v4.data.CommandProviderWrapper
import skywolf46.commandannotation.v4.initializer.Serializers
import skywolf46.commandannotation.v4.initializer.SignalCore
import skywolf46.extrautility.util.ClassUtil
import skywolf46.extrautility.util.MethodUtil
import skywolf46.extrautility.util.triggerEvent
import kotlin.reflect.KClass

object CommandAnnotationCore {
    private val provider = mutableMapOf<KClass<*>, CommandProviderWrapper>()
    private val converter = mutableMapOf<KClass<*>, AnnotationConverterWrapper<*>>()
    private val reducer = mutableMapOf<KClass<*>, AnnotationReducerWrapper<*>>()

    fun init() {
        println("CommandAnnotation-Core | Initializing..")
        val classFilter = ClassUtil.getCache()
        val methodFilter = MethodUtil.getCache()
        scanSetupAnnotation(classFilter)

        println("CommandAnnotation-Core | Registering default serializers..")
        Serializers.init()

        println("CommandAnnotation-Core | Initializing signal core..")
        SignalCore.init()

        println("CommandAnnotation-Core | Calling serializer signal..")
        SerializerRegisterSignal().triggerEvent()
    }

    private fun scanSetupAnnotation(classFilter: ClassUtil.ClassFilter) {
        scanAnnotationProcessorAnnotation(classFilter)
    }

    private fun scanAnnotationProcessorAnnotation(classFilter: ClassUtil.ClassFilter) {
        classFilter.filterImplementation(IAnnotationConverter::class.java).list.forEach { cls ->
            val converterInstance = (cls.newInstance() as IAnnotationConverter<Annotation>)
            converter[cls.kotlin] = AnnotationConverterWrapper<Annotation> {
                converterInstance.convertTo(it)
            }
        }
    }

    private fun scanCommandAnnotation(classFilter: ClassUtil.ClassFilter, methodFilter: MethodUtil.MethodFilter) {

    }
}