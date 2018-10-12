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
import java.util.stream.Collectors
import java.util.stream.Stream

class ComponentBinder(private val injector: Injector, private val binder: Binder) {
    fun newMethod(type: Class<*>): MethodBinder {
        return MethodBinder(type, injector, binder)
    }

    fun newClass(classes: Collection<Class<*>>): ClassBinder {
        return ClassBinder(classes, injector, binder)
    }

    fun newField(type: Class<*>): FieldBinder {
        return FieldBinder(type, injector, binder)
    }

    interface TransformBinder<T1> {
        fun <A : Annotation, T : Any> transform(
            annotation: Class<A>, to: Class<T>, map: (T1, A) -> Module
        ): InternalBinder<T>

        fun <A : Annotation, T : Any, S : AnnotatedElement> reform(
            injector: Injector, stream: Stream<S>, annotation: Class<A>, to: Class<T>, map: (T1, A) -> Module
        ): List<T> {
            return stream.map { element ->
                val a = element.getAnnotation(annotation)

                injector.createChildInjector(map(element as T1, a)).getInstance(to)
            }.collect(Collectors.toList())
        }
    }

    abstract class OptionalTransformBinder<T1>(protected val injector: Injector, protected val binder: Binder) :
        TransformBinder<T1> {
        private var allowed = true
        fun allow(allow: Boolean): OptionalTransformBinder<T1> {
            this.allowed = allow
            return this
        }

        fun <A : Annotation, T : Any, S : AnnotatedElement> optionalReform(
            injector: Injector, stream: Stream<S>, annotation: Class<A>, to: Class<T>, map: (T1, A) -> Module
        ): List<T> {
            if (!allowed && stream.count() > 0) {
                throw IllegalStateException("'${to.simpleName}' is not supported.")
            }
            return reform(injector, stream, annotation, to, map)
        }
    }

    class MethodBinder(private val type: Class<*>, injector: Injector, binder: Binder) :
        OptionalTransformBinder<Method>(injector, binder) {
        override fun <A : Annotation, T : Any> transform(
            annotation: Class<A>, to: Class<T>, map: (Method, A) -> Module
        ): InternalBinder<T> {
            return InternalBinder(
                optionalReform(
                    injector, Methods.annotatedMethods(type, annotation).stream(), annotation, to, map
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
                    injector, Fields.annotatedFields(type, annotation).stream(), annotation, to, map
                ), to, binder
            )
        }
    }

    class ClassBinder(private val classes: Collection<Class<*>>, injector: Injector, binder: Binder) :
        OptionalTransformBinder<Class<*>>(injector, binder) {
        override fun <A : Annotation, T : Any> transform(
            annotation: Class<A>, to: Class<T>, map: (Class<*>, A) -> Module
        ): InternalBinder<T> {
            return InternalBinder(optionalReform(injector, classes.stream(), annotation, to, map), to, binder)
        }
    }

    class InternalBinder<T> internal constructor(
        private val list: List<T>, private val type: Class<T>, private val binder: Binder
    ) {
        fun bind() {
            binder.bind(TypeLiteral.get(Types.setOf(type)) as TypeLiteral<Set<T>>).toInstance(Sets.newHashSet(list))
        }
    }
}
