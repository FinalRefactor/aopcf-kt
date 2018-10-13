package av.`is`.aopcfkt.patterns

import av.`is`.aopcfkt.ParameterMapper
import java.lang.reflect.Method

class NamedPattern(private val method: Method, private val pattern: String) {
    fun getPatterns(): List<PatternEntry<*>> {
        val parameters = method.parameterTypes
        val arguments = pattern.split(" ").filter { it.isNotEmpty() }

        if (parameters.size != arguments.filter { it.contains(":") }.size && method.getAnnotation(ParameterMapper::class.java) != null) {
            throw IllegalArgumentException("Mapper's parameters and pattern have to be same!")
        }

        var i = 0
        return arguments.map { pattern ->
            if (pattern.contains(":")) {
                PatternEntry(null, pattern.split(":")[1], parameters[i++])
            } else {
                PatternEntry(pattern, null, String::class.java)
            }
        }
    }
}
