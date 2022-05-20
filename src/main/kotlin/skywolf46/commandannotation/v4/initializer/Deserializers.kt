package skywolf46.commandannotation.v4.initializer

import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.commandannotation.v4.api.util.registerDeserializer


@Suppress("MemberVisibilityCanBePrivate")
object Deserializers {

    fun init() {
        String::class.registerDeserializer(STRING_DESERIALIZER)
        Byte::class.registerDeserializer(BYTE_DESERIALIZER)
        Short::class.registerDeserializer(SHORT_DESERIALIZER)
        Int::class.registerDeserializer(INT_DESERIALIZER)
        Long::class.registerDeserializer(LONG_DESERIALIZER)
        Float::class.registerDeserializer(FLOAT_DESERIALIZER)
        Double::class.registerDeserializer(DOUBLE_DESERIALIZER)
    }

    val STRING_DESERIALIZER: Arguments.() -> String = {
        requireCurrentState { length() > 0 }
        arg()!!
    }

    val BYTE_DESERIALIZER: Arguments.() -> Byte = {
        requireCurrentState { length() > 0 }
        expectCurrentState(NumberFormatException::class.java)
        arg()!!.toByte()
    }

    val SHORT_DESERIALIZER: Arguments.() -> Short = {
        requireCurrentState { length() > 0 }
        expectCurrentState(NumberFormatException::class.java)
        arg()!!.toShort()
    }

    val INT_DESERIALIZER: Arguments.() -> Int = {
        println(length())
        requireCurrentState { length() > 0 }
        expectCurrentState(NumberFormatException::class.java)
        arg()!!.toInt()
    }

    val LONG_DESERIALIZER: Arguments.() -> Long = {
        requireCurrentState { length() > 0 }
        expectCurrentState(NumberFormatException::class.java)
        arg()!!.toLong()
    }

    val FLOAT_DESERIALIZER: Arguments.() -> Float = {
        requireCurrentState { length() > 0 }
        expectCurrentState(NumberFormatException::class.java)
        arg()!!.toFloat()
    }

    val DOUBLE_DESERIALIZER: Arguments.() -> Double = {
        requireCurrentState { length() > 0 }
        expectCurrentState(NumberFormatException::class.java)
        arg()!!.toDouble()
    }

}



