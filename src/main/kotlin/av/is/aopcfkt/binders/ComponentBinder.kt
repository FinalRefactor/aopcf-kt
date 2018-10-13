package av.`is`.aopcfkt.binders

import av.`is`.aopcfkt.utils.Fields
import av.`is`.aopcfkt.utils.Methods
import com.google.common.collect.Sets
import com.google.inject.Binder
import com.google.inject.Injector
import com.google.inject.Module
import com.google.inject.TypeLiteral
import com.google.inject.util.Types
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Field
import java.lang.reflect.Method

class ComponentBinder(private val injector: Injector, private val binder: Binder) {
    fun newMethod(type: Class<*>): MethodBinder = MethodBinder(type, injector, binder)

    fun newClass(classes: Collection<Class<*>>): ClassBinder = ClassBinder(classes, injector, binder)

    fun newField(type: Class<*>): FieldBinder = FieldBinder(type, injector, binder)

    interface TransformBinder<T1> {
        fun <A : Annotation, T : Any> transform(
            annotation: Class<A>, to: Class<T>, map: (T1, A) -> Module
        ): InternalBinder<T>

        fun <A : Annotation, T : Any, S : AnnotatedElement> reform(
            injector: Injector, collection: Collection<S>, annotation: Class<A>, to: Class<T>, map: (T1, A) -> Module
        ): List<T> {
            return collection.map { element ->
                val a = element.getAnnotation(annotation)

                injector.createChildInjector(map(element as T1, a)).getInstance(to)
            }
        }
    }

    abstract class OptionalTransformBinder<T1>(protected val injector: Injector, protected val binder: Binder) :
        TransformBinder<T1> {
        private var allowed = true
        fun allow(allow: Boolean): OptionalTransformBinder<T1> {
            this.allowed = allow
            return this
        }

        inline fun <reified A : Annotation, reified T : Any> transform(noinline map: (T1, A) -> Module): ComponentBinder.InternalBinder<T> =
            transform(A::class.java, T::class.java, map)

        fun <A : Annotation, T : Any, S : AnnotatedElement> optionalReform(
            injector: Injector, collection: Collection<S>, annotation: Class<A>, to: Class<T>, map: (T1, A) -> Module
        ): List<T> {
            if (!allowed && collection.isNotEmpty()) {
                throw IllegalStateException("'${to.simpleName}' is not supported.")
            }
            return reform(injector, collection, annotation, to, map)
        }
    }

    class MethodBinder(private val type: Class<*>, injector: Injector, binder: Binder) :
        OptionalTransformBinder<Method>(injector, binder) {

        override fun <A : Annotation, T : Any> transform(
            annotation: Class<A>, to: Class<T>, map: (Method, A) -> Module
        ): InternalBinder<T> {
            return InternalBinder(
                optionalReform(
                    injector, Methods.annotatedMethods(type, annotation), annotation, to, map
                ), to, binder
            )
        }
    }

    class FieldBinder(private val type: Class<*>, injector: Injector, binder: Binder) :
        OptionalTransformBinder<Field>(injector, binder) {

        override fun <A : Annotation, T : Any> transform(
            annotation: Class<A>, to: Class<T>, map: (Field, A) -> Module
        ): InternalBinder<T> {
            return InternalBinder(
                optionalReform(
                    injector, Fields.annotatedFields(type, annotation), annotation, to, map
                ), to, binder
            )
        }
    }

    class ClassBinder(private val classes: Collection<Class<*>>, injector: Injector, binder: Binder) :
        OptionalTransformBinder<Class<*>>(injector, binder) {

        override fun <A : Annotation, T : Any> transform(
            annotation: Class<A>, to: Class<T>, map: (Class<*>, A) -> Module
        ): InternalBinder<T> {
            return InternalBinder(optionalReform(injector, classes, annotation, to, map), to, binder)
        }
    }

    class InternalBinder<T> internal constructor(
        private val list: List<T>, private val type: Class<T>, private val binder: Binder
    ) {
        fun bind() {
            binder.bind(TypeLiteral.get(Types.setOf(type)) as TypeLiteral<Set<T>>)
                .toInstance(Sets.newHashSet(list))
        }
    }
}
