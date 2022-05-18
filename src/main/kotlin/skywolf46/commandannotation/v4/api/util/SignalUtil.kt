package skywolf46.commandannotation.v4.api.util

import skywolf46.commandannotation.v4.initializer.SignalCore

fun <T : Any> T.callSignal() {
    SignalCore.callSignal(this)
}