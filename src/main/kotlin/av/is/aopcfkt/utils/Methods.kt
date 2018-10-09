package av.`is`.aopcfkt.utils

import java.lang.reflect.Method
import java.util.stream.Collectors

class Methods private constructor() {

    companion object {

        fun <T: Any, A: Annotation> annotatedMethods(type: Class<T>, annotation: Class<A>): MutableList<Method> {
            return type.declaredMethods.toList()
                    .stream()
                    .filter { method -> method.getAnnotation(annotation) != null }
                    .collect(Collectors.toList())
        }

    }

}