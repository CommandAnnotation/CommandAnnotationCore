package skywolf46.commandannotation.v4.test.data

import org.junit.jupiter.api.Assertions
import skywolf46.commandannotation.v4.api.data.Arguments
import skywolf46.commandannotation.v4.test.exceptions.TestSucceedException

object TestCommandContainer {
    @TestCommandAnnotation("/test1234")
    fun testCommand(args: Arguments) {

    }


    @TestCommandAnnotation("/test1234 asdf")
    fun testCommand2(args: Arguments) {

    }


    @TestCommandAnnotation("/test1234 asdf fdsa")
    fun testCommand3(args: Arguments) {

    }


    @TestCommandAnnotation("/test1234 asdf asdf")
    fun testCommand4(args: Arguments) {

    }

    @TestCommandAnnotation("/test1234 parameter <test>")
    fun parameterizedTestCommand01(args: Arguments) {
        Assertions.assertEquals("01", args.next())
        throw TestSucceedException("Test01")
    }


    @TestCommandAnnotation("/test1234 <test> parameter")
    fun parameterizedTestCommand02(args: Arguments) {
        Assertions.assertEquals("02", args.next())
        throw TestSucceedException("Test02")
    }


    @TestCommandAnnotation("/test1234 <test>")
    fun parameterizedFallbackTestCommand(args: Arguments) {
        throw TestSucceedException("TestFallback")
    }

    @TestCommandAnnotation("/test1234 stringTest")
    fun Arguments.stringParseTest() {
        next<String> {
            if (it == "test!")
                throw TestSucceedException()
        }
    }

    @TestCommandAnnotation("/test1234 stringTest2")
    fun Arguments.depthStringParseTest() {
        next<String> { str1 ->
            next<String> { str2 ->
                if (str1 == "Test1" && str2 == "Test2")
                    throw TestSucceedException()
            }
        }
    }


    @TestCommandAnnotation("/test1234 byteTest")
    fun Arguments.byteParseTest() {
        next<Byte> {
            if (it == 11.toByte())
                throw TestSucceedException()
        }
    }


    @TestCommandAnnotation("/test1234 byteTest2")
    fun Arguments.depthByteParseTest() {
        next<Byte> { byte1 ->
            next<Byte> { byte2 ->
                if (byte1 == 24.toByte() && byte2 == 60.toByte())
                    throw TestSucceedException()
            }
        }
    }


    @TestCommandAnnotation("/test1234 shortTest")
    fun Arguments.shortParseTest() {
        next<Short> {
            if (it == 1187.toShort())
                throw TestSucceedException()
        }
    }


    @TestCommandAnnotation("/test1234 shortTest2")
    fun Arguments.depthShortParseTest() {
        next<Short> { short1 ->
            next<Short> { short2 ->
                if (short1 == 9921.toShort() && short2 == 9378.toShort())
                    throw TestSucceedException()
            }
        }
    }


    @TestCommandAnnotation("/test1234 intTest")
    fun Arguments.intParseTest() {
        next<Int> {
            if (it == 49237) {
                throw TestSucceedException()
            }
        }
    }


    @TestCommandAnnotation("/test1234 intTest2")
    fun Arguments.depthIntParseTest() {
        next<Int> { int1 ->
            next<Int> { int2 ->
                if (int1 == 97867 && int2 == 28991)
                    throw TestSucceedException()
            }
        }
    }


    @TestCommandAnnotation("/test1234 floatTest")
    fun Arguments.floatParseTest() {
        next<Float> {
            if (it == 21390f)
                throw TestSucceedException()
        }
    }


    @TestCommandAnnotation("/test1234 floatTest2")
    fun Arguments.floatStringParseTest2() {
        next<Float> { float1 ->
            next<Float> { float2 ->
                if (float1 == 11978f && float2 == 52911f)
                    throw TestSucceedException()
            }
        }
    }


    @TestCommandAnnotation("/test1234 doubleTest")
    fun Arguments.doubleParseTest() {
        next<Double> {
            if (it == 26269.3)
                throw TestSucceedException()
        }
    }


    @TestCommandAnnotation("/test1234 doubleTest2")
    fun Arguments.doubleStringParseTest2() {
        next<Double> { double1 ->
            next<Double> { double2 ->
                if (double1 == 18344.7 && double2 == 88287.4)
                    throw TestSucceedException()
            }
        }
    }

    @TestCommandAnnotation("/test1234 longTest")
    fun Arguments.longParseTest() {
        next<Long> {
            if (it == 52519L)
                throw TestSucceedException()
        }
    }


    @TestCommandAnnotation("/test1234 longTest2")
    fun Arguments.depthLongParseTest() {
        next<Long> { long1 ->
            next<Long> { long2 ->
                if (long1 == 87816L && long2 == 73467L)
                    throw TestSucceedException()
            }
        }
    }


}