package skywolf46.commandannotation.kotlin.annotation

/**
 * Command condition checker.
 * When command execute, PreCondition markers will invoke first.
 * If returning value is `false`, command will be blocked.
 */
annotation class PreCondition() {
}