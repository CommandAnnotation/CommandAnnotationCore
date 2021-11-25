package skywolf46.commandannotation.stream

import java.io.*


object SuggestibleConsole {
    fun install(input: InputStream, output: OutputStream) {
        FileDescriptor.`in`.sync()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val buffer = BufferedReader(
            InputStreamReader(System.`in`))
        var c = 0
        while (buffer.read().also { c = it } != -1) {
            val character = c.toChar()
            println(character)
        }
    }
}