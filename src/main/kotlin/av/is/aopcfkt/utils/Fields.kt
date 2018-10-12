package av.`is`.aopcfkt.utils

import java.lang.reflect.Field

class Fields private constructor() {
    companion object {
        fun <T : Any, A : Annotation> annotatedFields(type: Class<T>, annotation: Class<A>): List<Field> {
            return type.declaredFields.filter { it.getAnnotation(annotation) != null }
        }
    }
}
