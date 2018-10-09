package av.`is`.aopcfkt.manifests

import av.`is`.aopcfkt.ParameterMapper
import av.`is`.aopcfkt.internal.Mapper
import av.`is`.aopcfkt.internal.impl.MapperImpl
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.name.Names
import java.lang.reflect.Method

class MapperManifest(private val method: Method,
                     private val mapper: ParameterMapper) : AbstractModule() {

    @Provides
    fun pattern(): String {
        return mapper.value
    }

    override fun configure() {
        bind(Mapper::class.java).to(MapperImpl::class.java)
        bind(Method::class.java).annotatedWith(Names.named("mapper")).toInstance(method)
    }
}