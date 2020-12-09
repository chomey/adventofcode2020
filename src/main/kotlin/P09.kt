import java.util.Collections.max
import java.util.Collections.min

fun main() {
    val numbers = load("P09.txt").map { it.toLong() }
    val preamble = 25L

    var i = preamble.toInt()
    var weak = 0L
    while (true) {
        if (!hasSum(numbers, preamble, i)) {
            weak = numbers[i]
            break
        }
        i++
    }
    println(weak)

    contiguousSum(numbers, i)
}

fun hasSum(numbers: List<Long>, preamble: Long, i: Int): Boolean {
    val list = numbers.subList(i - preamble.toInt(), i)
    val target = numbers[i]
    for (i in 0 until list.size) {
        for (j in i until list.size) {
            if (list[i] + list[j] == target) {
                return true
            }
        }
    }
    return false
}

fun contiguousSum(numbers: List<Long>, i: Int) {
    val list = numbers.subList(0, i)
    var left = 0
    var right = 1

    val target = numbers[i]
    while (right < list.size) {
        val sublist = list.subList(left, right)
        val sum = sublist.sum()
        when {
            sum == target -> {
                println(min(sublist) + max(sublist))
                return
            }
            sum < target -> {
                right++
            }
            else -> {
                left++
            }
        }
    }
}
