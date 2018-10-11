package av.`is`.aopcfkt.internal

import av.`is`.aopcfkt.patterns.PatternEntry
import java.util.*
import javax.inject.Inject

class Repository @Inject constructor(private val type: Class<*>,
                                     val instance: Any,
                                     mappers: MutableSet<Mapper>,
                                     private val commandContexts: MutableSet<CommandContext>) {

    private val mappers: MutableSet<Mapper> = mappers.also {
        it.forEach { mapper -> mapper.setRepository(this) }
    }

    fun prints() {
        println("type: ${type.simpleName}")
        println("mappers: ${mappers.size}")
        mappers.forEach { it.prints() }
        println("commands: ${commandContexts.size}")
        commandContexts.forEach { it.prints() }

        val o = getMapped(listOf(PatternEntry("my name"), PatternEntry("plus"), PatternEntry(5, Int::class.java)))
        println(Optional.ofNullable(o).orElse("null").toString())
    }

    fun getMapped(patternEntries: List<PatternEntry<*>>): Any? {
        for(mapper in mappers) {
            mapper.tryInvoke(patternEntries)?.let {
                return it
            }
        }
        return null
    }

}
