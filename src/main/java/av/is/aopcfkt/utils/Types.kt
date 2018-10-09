package av.`is`.aopcfkt.utils

import java.lang.reflect.AnnotatedElement
import java.util.*

class Types private constructor() {

    companion object {

        fun <T: Annotation> annotated(element: AnnotatedElement, annotation: Class<T>): Optional<T> {
            return Optional.ofNullable(element.getAnnotation(annotation))
        }

        fun <T: Annotation> annotatedChain(element: AnnotatedElement, annotation: Class<T>): ChainingOptional<T> {
            return chain(annotated(element, annotation))
        }

        fun <T: Any> chain(optional: Optional<T>): ChainingOptional<T> {
            return ChainingOptional.of(optional)
        }

    }

}