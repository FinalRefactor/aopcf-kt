package av.`is`.aopcfkt.example

import av.`is`.aopcfkt.Command
import av.`is`.aopcfkt.CommandRepository
import av.`is`.aopcfkt.ParameterMapper

@CommandRepository
class ExampleRepository {
    @ParameterMapper(":name :amount")
    fun c(name: String, amount: Int): Example {
        println("c()")
        return Example(name, amount)
    }

    @ParameterMapper(":amount1 :amount2")
    fun d(amount1: Int, amount2: Int): Example {
        println("d()")
        return Example(amount1.toString(), amount2)
    }

    @ParameterMapper(":name plus :amount")
    fun f(name: String, amount: Int): Example {
        println("f()")
        return Example(name, amount + amount)
    }

    @ParameterMapper(":name1 :name2")
    fun e(name1: String, name2: String): Example {
        println("e()")
        val i = name2.toIntOrNull() ?: 0

        return Example(name1, i)
    }

    @Command("example :name :amount")
    fun command(example: Example) {

    }
}
