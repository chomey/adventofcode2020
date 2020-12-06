fun main() {
    val lines: List<String> = load("P6.txt").joinToString(separator = " ").split("  ")
    var sum = 0L
    lines.forEach { sum += it.replace(" ", "").chars().distinct().count() }
    println(sum)

    var sum2 = 0L
    lines.forEach { line ->
        val raw = line.split(" ")
        sum2 += raw.first().filter { char -> raw.all { it.contains(char) } }.length
    }
    println(sum2)
}