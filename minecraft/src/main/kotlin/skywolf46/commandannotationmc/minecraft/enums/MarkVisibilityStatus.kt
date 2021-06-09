package skywolf46.commandannotationmc.minecraft.enums

enum class MarkVisibilityStatus {
    /**
     * **Global visibility.**
     * Mark will visible in all project.
     */
    GLOBAL,

    /**
     * **Project visibility.**
     * Mark will be visible in same package in top two level.
     * As example, PROJECT mark in [skywolf46.commandannotationmc] is not visible to [skywolf46.commandannotation], but visible to [skywolf46.commandannotationmc.impl].
     */
    PROJECT,

    /**
     * **Class visibility.**
     * Mark will be visible in same class.
     */
    CLASS
}