package av.`is`.aopcfkt.manifests

import av.`is`.aopcfkt.CommandRepository
import av.`is`.aopcfkt.binders.ComponentBinder
import av.`is`.aopcfkt.example.ExampleRepository
import av.`is`.aopcfkt.internal.Repositories
import av.`is`.aopcfkt.internal.Repository
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector

class CoreManifest(private val injector: Injector, private val classes: Array<Class<*>>) : AbstractModule() {
    override fun configure() {
        val binder = ComponentBinder(injector, binder())
        binder.newClass(classes.toList()).transform<CommandRepository, Repository> { element, _ ->
            RepositoryManifest(injector, element)
        }.bind()
    }
}

fun main(args: Array<String>) {
    val injector = Guice.createInjector()
    val repositories = injector.createChildInjector(CoreManifest(injector, arrayOf(ExampleRepository::class.java)))
        .getInstance(Repositories::class.java)

    repositories.prints()
}
