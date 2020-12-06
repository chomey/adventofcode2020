fun main() {
    val lines: List<String> = loadDoubleSpaced("P6.txt")
    var sum = 0L
    lines.forEach { sum += it.replace(" ", "").chars().distinct().count() }
    println(sum)

    var sum2 = 0L
    lines.forEach { line ->
        val raw = line.split(" ")
        var intersect = raw.first().toHashSet()
        raw.forEach { intersect = intersect.intersect(it.toHashSet()).toHashSet() }
        sum2 += intersect.size
    }
    println(sum2)
}