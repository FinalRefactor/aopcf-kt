package av.`is`.aopcfkt.internal.impl

import av.`is`.aopcfkt.internal.Mapper
import av.`is`.aopcfkt.internal.Repository
import av.`is`.aopcfkt.patterns.NamedPattern
import av.`is`.aopcfkt.patterns.PatternEntry
import com.google.common.base.Objects
import java.lang.reflect.Method
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class MapperImpl @Inject constructor(@Named("mapper") private val method: Method,
                                     private val pattern: String) : Mapper {

    private val entries: List<PatternEntry<*>>
    private var repository: Repository? = null

    init {
        entries = NamedPattern(method, pattern).getPatterns()
    }

    override fun tryInvoke(patternEntries: List<PatternEntry<*>>): Any? {
        if(entries.size != patternEntries.size) {
            return null
        }
        val arguments = mutableListOf<Any>()
        for(i in 0 until entries.size) {
            val a = entries[i]
            val b = patternEntries[i]
            if(!a.matches(b)) {
                return null
            }
            if(a.getPattern() != null) {
                arguments += b.getArgument()!!
            }
        }

        method.isAccessible = true
        return method.invoke(repository!!.getInstance(), *arguments.toTypedArray())
    }

    override fun setRepository(repository: Repository) {
        this.repository = repository
    }

    override fun returnType(): Class<*> {
        return method.returnType
    }

    override fun prints() {
        println(" - method: $method")
        println(" - pattern: $pattern")
    }

    override fun hashCode(): Int {
        return Objects.hashCode(method, pattern, entries, repository)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MapperImpl

        if (method != other.method) return false
        if (pattern != other.pattern) return false
        if (entries != other.entries) return false
        if (repository != other.repository) return false

        return true
    }
}