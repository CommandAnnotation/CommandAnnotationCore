package skywolf46.commandannotation.v4.api.exceptions

class CommandFailedException : RuntimeException {

    constructor() : super()

    constructor(msg: String) : super(msg)
}