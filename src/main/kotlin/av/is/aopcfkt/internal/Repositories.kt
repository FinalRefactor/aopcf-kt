package av.`is`.aopcfkt.internal

import javax.inject.Inject

class Repositories {

    @Inject internal var repositories: Set<Repository>? = null

    fun prints() {
        println("repositories: ${repositories!!.size}")
        repositories!!.forEach(Repository::prints)
    }

}