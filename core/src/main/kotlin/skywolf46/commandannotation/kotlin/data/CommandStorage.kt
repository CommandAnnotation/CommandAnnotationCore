package skywolf46.commandannotation.kotlin.data

import skywolf46.commandannotation.kotlin.abstraction.ICommand

class CommandStorage(private val accepting: Class<ICommand>) {
}