fun main() {
    var test = false
    //Comment out
//    test = true
    val input = if (test) {
        "389125467"
    } else {
        "963275481"
    }.map { ("" + it).toInt() }.toMutableList()
    val max = 1_000_000
    for (i in input.size + 1..max) {
        input.add(i)
    }

    val left = mutableMapOf<Int, Int>()
    val right = mutableMapOf<Int, Int>()
    left[input[0]] = input[input.size - 1]
    right[input[0]] = input[1]
    for (i in 1 until input.size - 1) {
        left[input[i]] = input[i - 1]
        right[input[i]] = input[i + 1]
    }
    left[input[input.size - 1]] = input[input.size - 2]
    right[input[input.size - 1]] = input[0]

    var curr = input[0]

    for (move in 1..(max * 10)) {
        val next1 = right[curr]!!
        val next2 = right[next1]!!
        val next3 = right[next2]!!
        val next4 = right[next3]!!
        right[curr] = next4
        left[next4] = curr

        val d = findDown(setOf(next1, next2, next3), left.size, curr)
        val dRight = right[d]!!

        right[d] = next1
        left[next1] = next2

        left[dRight] = next3
        right[next3] = dRight
        curr = right[curr]!!
    }

    val next = right[1]!!
    val next2 = right[next]!!
    println(next.toLong() * next2.toLong())
}

fun findDown(invalidVals: Set<Int>, size: Int, i: Int): Int {
    var curr = boundDown(size, i - 1)
    while (invalidVals.contains(curr)) {
        curr = boundDown(size, curr - 1)
    }
    return curr
}

fun boundDown(size: Int, i: Int): Int {
    return if (i == 0) {
        size
    } else {
        i
    }
}

