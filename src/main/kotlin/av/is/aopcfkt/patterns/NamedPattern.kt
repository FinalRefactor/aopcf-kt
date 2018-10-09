package av.`is`.aopcfkt.patterns

import av.`is`.aopcfkt.ParameterMapper
import java.lang.IllegalArgumentException
import java.lang.reflect.Method
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Collectors

class NamedPattern(private val method: Method,
                   private val pattern: String) {

    fun getPatterns(): MutableList<PatternEntry<*>> {
        val parameters = method.parameterTypes
        val arguments = pattern.split(" ").toList()
                .stream()
                .filter(String::isNotEmpty)
                .collect(Collectors.toList())

        if(parameters.size.toLong() != arguments.stream().filter { s -> s.contains(":") }.count() && method.getAnnotation(ParameterMapper::class.java) != null) {
            throw IllegalArgumentException("Mapper's parameters and pattern have to be same!")
        }

        val increment = AtomicInteger(0)
        return arguments.stream().map { pattern ->
            if(pattern.contains(":")) {
                val i = increment.getAndIncrement()
                PatternEntry(null, pattern.split(":")[1], parameters[i])
            } else {
                PatternEntry(pattern, null, String::class.java)
            }
        }.collect(Collectors.toList())
    }

}