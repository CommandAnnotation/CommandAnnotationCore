package skywolf46.commandannotation.v4.initializer

import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.commandannotation.v4.api.util.registerSerializer


@Suppress("MemberVisibilityCanBePrivate")
object Serializers {

    fun init() {
        Byte::class.registerSerializer(BYTE_SERIALIZER)
        Short::class.registerSerializer(SHORT_SERIALIZER)
        Int::class.registerSerializer(INT_SERIALIZER)
        Long::class.registerSerializer(LONG_SERIALIZER)
        Float::class.registerSerializer(FLOAT_SERIALIZER)
        Double::class.registerSerializer(DOUBLE_SERIALIZER)
    }

    val BYTE_SERIALIZER: Arguments.() -> Byte = {
        requireCurrentState { length() > 0 }
        expectCurrentState(NumberFormatException::class.java)
        peekArg()!!.toByte()
    }

    val SHORT_SERIALIZER: Arguments.() -> Short = {
        requireCurrentState { length() > 0 }
        expectCurrentState(NumberFormatException::class.java)
        peekArg()!!.toShort()
    }

    val INT_SERIALIZER: Arguments.() -> Int = {
        requireCurrentState { length() > 0 }
        expectCurrentState(NumberFormatException::class.java)
        peekArg()!!.toInt()
    }

    val LONG_SERIALIZER: Arguments.() -> Long = {
        requireCurrentState { length() > 0 }
        expectCurrentState(NumberFormatException::class.java)
        peekArg()!!.toLong()
    }

    val FLOAT_SERIALIZER: Arguments.() -> Float = {
        requireCurrentState { length() > 0 }
        expectCurrentState(NumberFormatException::class.java)
        peekArg()!!.toFloat()
    }

    val DOUBLE_SERIALIZER: Arguments.() -> Double = {
        requireCurrentState { length() > 0 }
        expectCurrentState(NumberFormatException::class.java)
        peekArg()!!.toDouble()
    }

}



