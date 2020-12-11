import java.util.Collections.max
import java.util.Collections.sort

fun main() {
    val lines = load("P10.txt").map { it.toInt() }.toMutableList()
    lines.add(0)
    lines.add(max(lines) + 3)

    sort(lines)

    var ones = 0
    var threes = 0
    for (i in 0 until lines.size - 1) {
        val curr = lines[i]
        val next = lines[i + 1]
        when (next - curr) {
            1 -> ones++
            3 -> threes++
        }

    }
    println(ones * threes)

    val dp = Array(lines.size) { 0L }
    dp[0] = 1
    for (i in 1 until lines.size) {
        var sum = 0L
        for (j in 1 until kotlin.math.min(i, 3) + 1) {
            if (lines[i] - lines[i - j] <= 3) {
                sum = Math.addExact(sum, dp[i - j])
            }
        }
        dp[i] = sum
    }
    println(dp[lines.size - 1])
}
