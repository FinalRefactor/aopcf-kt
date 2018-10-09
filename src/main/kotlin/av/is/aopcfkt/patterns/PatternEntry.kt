package av.`is`.aopcfkt.patterns

import com.google.common.base.Objects

class PatternEntry<T : Any> {

    private val argument: T?
    private val pattern: String?
    private val parameterType: Class<T>

    constructor(argument: T?, pattern: String?, parameterType: Class<T>) {
        this.argument = argument
        this.pattern = pattern
        this.parameterType = parameterType
    }

    constructor(argument: T): this(argument, argument.javaClass)

    constructor(argument: T, parameterType: Class<T>) {
        this.argument = argument
        this.pattern = null
        this.parameterType = parameterType
    }

    fun getArgument(): T? {
        return argument
    }

    fun getPattern(): String? {
        return pattern
    }

    override fun hashCode(): Int {
        return Objects.hashCode(argument, pattern, parameterType)
    }

    fun matches(argument: PatternEntry<*>): Boolean {
        var matches = if(getArgument() != null) {
            getArgument()!! == argument.getArgument()
        } else {
            true
        }
        matches = matches && parameterType == argument.parameterType
        return matches
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PatternEntry<*>

        if (argument != other.argument) return false
        if (pattern != other.pattern) return false
        if (parameterType != other.parameterType) return false

        return true
    }

}