package av.`is`.aopcfkt.manifests

import av.`is`.aopcfkt.Command
import av.`is`.aopcfkt.internal.CommandContext
import av.`is`.aopcfkt.internal.impl.CommandContextImpl
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.name.Names
import java.lang.reflect.Method

class CommandManifest(private val method: Method,
                      private val command: Command) : AbstractModule() {

    @Provides fun command(): Command {
        return command
    }

    override fun configure() {
        bind(CommandContext::class.java).to(CommandContextImpl::class.java)
        bind(Method::class.java).annotatedWith(Names.named("command")).toInstance(method)
    }

}