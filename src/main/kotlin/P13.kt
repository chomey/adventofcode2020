fun main() {
    val lines = load("P13.txt")

    val num = lines[0].toLong()
    val buses = lines[1].split(",").filter { it != "x" }.map { it.toLong() }
    println(buses)

    var earliest = Long.MAX_VALUE
    var earliestBus = 0L
    buses.forEach {
        val times: Long = if (num % it == 0L) {
            num / it
        } else {
            (num / it) + 1
        }

        val potential = it * times
        if (potential < earliest) {
            earliest = potential
            earliestBus = it
        }
    }
    println((earliest - num) * earliestBus)
}

