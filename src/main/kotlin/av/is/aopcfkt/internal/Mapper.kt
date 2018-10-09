package av.`is`.aopcfkt.internal

import av.`is`.aopcfkt.patterns.PatternEntry

interface Mapper {

    fun prints()

    fun tryInvoke(patternEntries: List<PatternEntry<*>>): Any?

    fun setRepository(repository: Repository)

    fun returnType(): Class<*>

}