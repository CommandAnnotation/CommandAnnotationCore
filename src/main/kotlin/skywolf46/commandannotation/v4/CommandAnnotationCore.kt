package skywolf46.commandannotation.v4

import skywolf46.commandannotation.v4.api.abstraction.IAnnotationConverter
import skywolf46.commandannotation.v4.data.AnnotationConverterWrapper
import skywolf46.commandannotation.v4.data.AnnotationReducerWrapper
import skywolf46.commandannotation.v4.data.CommandProviderWrapper
import skywolf46.extrautility.util.ClassUtil
import skywolf46.extrautility.util.MethodUtil
import kotlin.reflect.KClass

object CommandAnnotationCore {
    private val provider = mutableMapOf<KClass<*>, CommandProviderWrapper>()
    private val converter = mutableMapOf<KClass<*>, AnnotationConverterWrapper<*>>()
    private val reducer = mutableMapOf<KClass<*>, AnnotationReducerWrapper<*>>()

    fun init() {
        println("CommandAnnotation Core | Initializing..")
        val classFilter = ClassUtil.getCache()
        val methodFilter = MethodUtil.getCache()

    }

    private fun scanSetupAnnotation(classFilter: ClassUtil.ClassFilter) {

    }

    private fun scanAnnotationProfessorAnnotation(classFilter: ClassUtil.ClassFilter) {
        classFilter.filterImplementation(IAnnotationConverter::class.java).list.forEach { cls ->
            val converterInstance = (cls.newInstance() as IAnnotationConverter<Annotation>)
            converter[cls.kotlin] = AnnotationConverterWrapper<Annotation> {
                converterInstance.convertTo(it)
            }
        }
    }
}