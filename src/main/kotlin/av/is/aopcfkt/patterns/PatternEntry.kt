package av.`is`.aopcfkt.patterns

import com.google.common.base.Objects

class PatternEntry<T : Any> {
    val argument: T?
    val pattern: String?
    private val parameterType: Class<T>

    constructor(argument: T?, pattern: String?, parameterType: Class<T>) {
        this.argument = argument
        this.pattern = pattern
        this.parameterType = parameterType
    }

    constructor(argument: T) : this(argument, argument.javaClass)
    constructor(argument: T, parameterType: Class<T>) {
        this.argument = argument
        this.pattern = null
        this.parameterType = parameterType
    }

    override fun hashCode(): Int {
        return Objects.hashCode(argument, pattern, parameterType)
    }

    fun matches(argument: PatternEntry<*>): Boolean {
        return this.argument?.let { it == argument.argument } ?: true && parameterType == argument.parameterType
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PatternEntry<*>

        return argument == other.argument && pattern == other.pattern && parameterType == other.parameterType
    }
}
