package skywolf46.commandannotation.kotlin.annotation

/**
 * A compact condition definer.
 * If return type is List<String>, CommandAnnotation will detect method as auto complete provider.
 * If return type is Boolean, CommandAnnotation will register method as condition.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class CompactCondition(val completer: String)