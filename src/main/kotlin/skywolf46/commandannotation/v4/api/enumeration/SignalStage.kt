package skywolf46.commandannotation.v4.api.enumeration

enum class SignalStage {
    /**
     * First ordered priority of pre-priority..
     * Always invoke first.
     */
    INITIAL,

    /**
     * Second ordered priority of pre-priority.
     */
    EARLIEST,

    /**
     * Third ordered priority of pre-priority.
     */
    EARLY,

    /**
     * Base executing priority.
     * Most of the work will execute on [CURRENT] stage.
     */
    CURRENT,

    /**
     * First ordered priority of post-priority.
     */
    LATE,

    /**
     * Second ordered priority of post-priority.
     */
    LATEST,

    /**
     * Third ordered priority of post-priority.
     * Always invoke last.
     */
    FINALIZE
}