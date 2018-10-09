package av.`is`.aopcfkt.utils

import java.lang.reflect.Field
import java.util.stream.Collectors

class Fields private constructor() {

    companion object {

        fun <T: Any, A: Annotation> annotatedFields(type: Class<T>, annotation: Class<A>): MutableList<Field> {
            return type.declaredFields.toList()
                    .stream()
                    .filter { field -> field.getAnnotation(annotation) != null }
                    .collect(Collectors.toList())
        }

    }

}