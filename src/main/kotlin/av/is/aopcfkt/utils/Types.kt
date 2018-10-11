package av.`is`.aopcfkt.utils

import java.lang.reflect.AnnotatedElement

class Types private constructor() {

    companion object {

        fun <T: Annotation> annotated(element: AnnotatedElement, annotation: Class<T>): T? {
            return element.getAnnotation(annotation)
        }

    }

}
