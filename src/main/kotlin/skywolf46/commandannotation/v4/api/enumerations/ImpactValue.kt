package skywolf46.commandannotation.v4.api.enumerations

/**
 * "Impact" value of current function.
 * [ImpactValue] is simple range definition of utility function, like conditions.
 */
enum class ImpactValue {
    /**
     * None of element will be impacted by this annotation.
     */
    NONE,

    /**
     * Whole class will be impacted by this annotation.
     */
    CLASS,

    /**
     * Class in same package will be impacted by this annotation.
     */
    PACKAGE,

    /**
     * Class in project(First package in which the class exists -
     *  ex) skywolf46.commandannotation.v4) will be impacted by this annotation.
     */
    PROJECT,

    /**
     * All class in runtime will be impacted by this annotation.
     */
    GLOBAL,
}