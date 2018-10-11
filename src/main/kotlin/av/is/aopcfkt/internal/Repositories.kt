package av.`is`.aopcfkt.internal

import javax.inject.Inject

class Repositories {

    @Inject internal var repositories: Set<Repository>? = null

    fun prints() {
        val repositories = repositories as Set<Repository>

        println("repositories: ${repositories.size}")
        repositories.forEach { it.prints() }
    }

}
