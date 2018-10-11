package av.`is`.aopcfkt.utils

import java.lang.reflect.Method

class Methods private constructor() {

    companion object {

        fun <T: Any, A: Annotation> annotatedMethods(type: Class<T>, annotation: Class<A>): List<Method> {
            return type.declaredMethods.filter { it.getAnnotation(annotation) != null }
        }

    }

}
