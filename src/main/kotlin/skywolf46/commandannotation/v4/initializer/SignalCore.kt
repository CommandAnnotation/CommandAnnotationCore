package skywolf46.commandannotation.v4.initializer

import skywolf46.commandannotation.v4.api.annotations.SignalListener
import skywolf46.commandannotation.v4.api.annotations.debug.AddonDevelopmentMethod
import skywolf46.commandannotation.v4.api.enumeration.SignalStage
import skywolf46.extrautility.core.enumeration.reflection.MethodFilter
import skywolf46.extrautility.core.util.AutoRegistrationUtil
import skywolf46.extrautility.core.util.ReflectionUtil
import skywolf46.extrautility.core.util.asSingletonCallable

object SignalCore {
    private val listener = mutableMapOf<Class<*>, MutableMap<SignalStage, MutableSet<(Any) -> Unit>>>()

    fun init() {
        AutoRegistrationUtil.getMethodCache()
            .requires(SignalListener::class.java)
            .filter(MethodFilter.INSTANCE_NOT_REQUIRED)
            .unlock()
            .forEach {
                registerSignalListener(it.asSingletonCallable())
            }
    }

    @AddonDevelopmentMethod
    fun <T : Any> registerSignalListener(cls: Class<T>, stage: SignalStage, unit: (T) -> Unit): UnregisterTrigger {
        listener.getOrPut(cls) {
            mutableMapOf()
        }.getOrPut(stage) {
            LinkedHashSet()
        }.add(unit as (Any) -> Unit)
        return UnregisterTrigger {
            unregisterSignalListener(cls, stage, unit)
        }
    }

    @AddonDevelopmentMethod
    fun callSignal(any: Any) {
        listener[any::class.java]?.apply {
            SignalStage.values().forEach {
                this[it]?.forEach { invoker ->
                    try {
                        invoker.invoke(any)
                    } catch (throwable: Throwable) {
                        throwable.printStackTrace()
                    }
                }
            }
        }
    }

    @AddonDevelopmentMethod
    fun registerSignalListener(method: ReflectionUtil.CallableFunction): UnregisterTrigger {
        val annotation = method.getAnnotation<SignalListener>() ?: throw SignalRegisterFailedException(
            "No annotation detected for method ${method.getFullName()}"
        )
        when (method.parameterCount()) {
            0 -> throw SignalRegisterFailedException("Failed to register signal listener ${method.getFullName()} : No parameter detected, signal listener requires 1 parameter to listen")
            1 -> {
                val methodInvoker: (Any) -> Unit = {
                    method.invoke(listOf(it))
                }
                val cls = method.parameter()[0].type
                annotation.stage.forEach {
                    listener.getOrPut(cls) {
                        mutableMapOf()
                    }.getOrPut(it) {
                        LinkedHashSet()
                    } += methodInvoker
                }
                return UnregisterTrigger {
                    annotation.stage.forEach {
                        unregisterSignalListener(cls, it, methodInvoker)
                    }
                }
            }
            else -> {
                throw SignalRegisterFailedException("Failed to register signal listener ${method.getFullName()} : Too many parameter detected, signal listener requires 1 parameter to listen")
            }
        }
    }

    @AddonDevelopmentMethod
    fun unregisterSignalListener(targetClass: Class<out Any>, stage: SignalStage, unit: (Any) -> Unit): Boolean {
        return listener[targetClass]?.get(stage)?.remove(unit) ?: false
    }

    class UnregisterTrigger internal constructor(private val unit: () -> Unit) {
        @AddonDevelopmentMethod
        fun trigger() {
            unit()
        }
    }

    class SignalRegisterFailedException(msg: String) : RuntimeException(msg)
}