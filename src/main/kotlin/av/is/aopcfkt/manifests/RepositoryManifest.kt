package av.`is`.aopcfkt.manifests

import av.`is`.aopcfkt.Command
import av.`is`.aopcfkt.CommandRepository
import av.`is`.aopcfkt.ParameterMapper
import av.`is`.aopcfkt.binders.ComponentBinder
import av.`is`.aopcfkt.internal.CommandContext
import av.`is`.aopcfkt.internal.Mapper
import av.`is`.aopcfkt.utils.Types
import com.google.inject.AbstractModule
import com.google.inject.Injector
import com.google.inject.Provides

class RepositoryManifest(private val injector: Injector,
                         private val type: Class<*>) : AbstractModule() {

    @Provides fun repositoryType(): Class<*> {
        return type
    }

    @Provides fun repositoryInstance(): Any {
        return injector.getInstance(type)
    }

    override fun configure() {
        val chaining = Types.annotated(type, CommandRepository::class.java)
        chaining?.let { commandRepository ->
            val binder = ComponentBinder(injector, binder())
            binder.newMethod(type)
                    .allow(commandRepository.mappers)
                    .transform(ParameterMapper::class.java, Mapper::class.java, ::MapperManifest).bind()

            binder.newMethod(type)
                    .allow(commandRepository.commands)
                    .transform(Command::class.java, CommandContext::class.java, ::CommandManifest).bind()

        } ?: throw IllegalArgumentException("Command repository requires @CommandRepository annotation.")
    }
}
