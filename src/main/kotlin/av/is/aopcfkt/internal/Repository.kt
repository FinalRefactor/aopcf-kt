package av.`is`.aopcfkt.internal

import av.`is`.aopcfkt.patterns.PatternEntry
import java.util.*
import java.util.stream.Collectors
import javax.inject.Inject

class Repository @Inject constructor(private val type: Class<*>,
                                     private val instance: Any,
                                     mappers: MutableSet<Mapper>,
                                     private val commandContexts: MutableSet<CommandContext>) {

    private val mappers: MutableSet<Mapper>

    init {
        this.mappers = mappers.stream().peek { mapper -> mapper.setRepository(this)}.collect(Collectors.toSet())
    }

    fun getInstance(): Any {
        return instance
    }

    fun prints() {
        println("type: ${type.simpleName}")
        println("mappers: ${mappers.size}")
        mappers.forEach(Mapper::prints)
        println("commands: ${commandContexts.size}")
        commandContexts.forEach(CommandContext::prints)

        val o = getMapped(listOf(PatternEntry("my name"), PatternEntry("plus"), PatternEntry(5, Int::class.java)))
        println(Optional.ofNullable(o).orElse("null").toString())
    }

    fun getMapped(patternEntries: List<PatternEntry<*>>): Any? {
        for(mapper in mappers) {
            val obj: Any? = mapper.tryInvoke(patternEntries)
            if(obj != null) {
                return obj
            }
        }
        return null
    }

}