package av.`is`.aopcfkt.utils

import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class ChainingOptional<T> private constructor(private val optional: Optional<T>) {

    companion object {

        fun <T: Any> of(optional: Optional<T>): ChainingOptional<T> {
            return ChainingOptional(optional)
        }

    }

    fun ifPresent(consumer: (T) -> Unit): OrElseOptional {
        val passed = AtomicBoolean(false)
        optional.ifPresent { t ->
            passed.set(true)
            consumer(t)
        }
        return OrElseOptional(passed)
    }

    class OrElseOptional internal constructor(private val passed: AtomicBoolean) {

        fun orElse(runnable: Runnable) {
            if(!passed.get()) {
                runnable.run()
            }
        }

    }

}