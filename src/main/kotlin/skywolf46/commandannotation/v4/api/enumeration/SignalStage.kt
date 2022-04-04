package skywolf46.commandannotation.v4.api.enumeration

enum class SignalStage {
    /**
     * Will execute before signal called.
     * Always executed before signal proceed.
     */
    PRE,

    /**
     * Will execute when the signal event is proceeded.
     * Although it is not guaranteed whether the signal event is before or after the firing,
     * It always executed after [SignalStage.PRE] and before [SignalStage.POST].
     */
    CURRENT,

    /**
     * Will execute after signal event proceeded.
     * Always executed after signal proceed.
     */
    POST
}