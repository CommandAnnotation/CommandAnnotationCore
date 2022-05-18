package skywolf46.commandannotation.v4.test.data

import skywolf46.commandannotation.v4.api.annotations.ArgumentRemapper
import skywolf46.commandannotation.v4.api.data.Arguments

object TestParameterRemapper {
    @ArgumentRemapper("test")
    fun test(str: String) : String {
        return str.substring(str.indexOf('_') + 1)
    }
}