package av.`is`.aopcfkt.internal.impl

import av.`is`.aopcfkt.Command
import av.`is`.aopcfkt.internal.CommandContext
import com.google.common.base.Objects
import java.lang.reflect.Method
import javax.inject.Inject
import javax.inject.Named

class CommandContextImpl @Inject constructor(@Named("command") private val method: Method,
                                             private val command: Command) : CommandContext {

    override fun hashCode(): Int {
        return Objects.hashCode(method, command)
    }

    override fun prints() {
        println(" - method: $method")
        println(" - command: $command")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CommandContextImpl

        return method == other.method && command == other.command
    }
}
