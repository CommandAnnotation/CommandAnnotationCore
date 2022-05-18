package skywolf46.commandannotation.v4.api.exceptions

class ExpectedException(throwable: Throwable) :
    RuntimeException("Expected exception '${throwable.javaClass.name}' thrown")