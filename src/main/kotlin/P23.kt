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

    val positions = mutableMapOf<Int, Pair<Int, Int>>()
    positions[input[0]] = input[input.size - 1] to input[1]
    for (i in 1 until input.size - 1) {
        positions[input[i]] = input[i - 1] to input[i + 1]
    }
    positions[input[input.size - 1]] = input[input.size - 2] to input[0]

    var curr = input[0]

    for (move in 1..(max * 10)) {
        val next1 = positions[curr]!!.second
        val next2 = positions[next1]!!.second
        val next3 = positions[next2]!!.second
        val next4 = positions[next3]!!.second
        positions[curr] = positions[curr]!!.first to next4
        positions[next4] = curr to positions[next4]!!.second

        val d = findDown(setOf(next1, next2, next3), positions.size, curr)
        val dRight = positions[d]!!.second

        positions[d] = positions[d]!!.first to next1
        positions[next1] = d to next2

        positions[dRight] = next3 to positions[dRight]!!.second
        positions[next3] = next2 to dRight
        curr = positions[curr]!!.second
    }

    val next = positions[1]!!.second
    val next2 = positions[next]!!.second
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

