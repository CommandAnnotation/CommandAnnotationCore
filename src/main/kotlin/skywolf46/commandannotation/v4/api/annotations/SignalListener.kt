package skywolf46.commandannotation.v4.api.annotations

import skywolf46.commandannotation.v4.api.enumeration.SignalStage

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class SignalListener(val stage: SignalStage = SignalStage.CURRENT)