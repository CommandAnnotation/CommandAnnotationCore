package skywolf46.commandannotation.v4.api.abstraction

import skywolf46.commandannotation.v4.api.data.Arguments

abstract class AbstractExpectedSerializer<T : Any> : (Arguments) -> T {
}