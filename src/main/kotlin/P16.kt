fun main() {
    val lines = load("P16.txt").joinToString("\n").split("\n\n")

    val specValues = parseRanges(lines)
    val validTickets = sortInvalidTickets(lines, specValues)

    val myTicket = lines[1].split("\n")[1].split(",").map { it.toInt() }
    validTickets.add(myTicket)

    val possibleCategories = computePossibleCategories(specValues, validTickets)
    topologicalSortPossible(possibleCategories)

    println(computeProduct(possibleCategories.map { it.first() }, myTicket))
}

private fun computeProduct(possibleCategories: List<String>, myTicket: List<Int>): Long {
    var product = 1L
    for (i in possibleCategories.indices) {
        val category = possibleCategories[i]
        if (category.startsWith("departure")) {
            product *= myTicket[i]
        }
    }
    return product
}

private fun topologicalSortPossible(possibleCategories: MutableList<MutableSet<String>>) {
    while (possibleCategories.any { it.size != 1 }) {
        possibleCategories.filter { it.size == 1 }.forEach {
            val lockedIn = it.first()
            possibleCategories.filter { it2 -> it != it2 }.forEach { it2 ->
                it2.remove(lockedIn)
            }
        }
    }
}

private fun computePossibleCategories(
    specValues: MutableMap<String, MutableSet<Pair<Int, Int>>>,
    validTickets: MutableSet<List<Int>>
): MutableList<MutableSet<String>> {
    val possibleCategories = mutableListOf<MutableSet<String>>()
    repeat(validTickets.first().size) { possibleCategories.add(specValues.keys.toMutableSet()) }

    possibleCategories.indices.forEach {
        val possibleForThis = possibleCategories[it]
        val iter = possibleForThis.iterator()
        while (iter.hasNext()) {
            val curr = iter.next()
            val isValid = validTickets.all { validTicket ->
                specValues[curr]!!.any { (min, max) -> validTicket[it] in min..max }
            }
            if (!isValid) {
                iter.remove()
            }
        }
    }
    return possibleCategories
}

private fun sortInvalidTickets(
    lines: List<String>,
    specValues: MutableMap<String, MutableSet<Pair<Int, Int>>>
): MutableSet<List<Int>> {
    var sum = 0
    val validTickets = mutableSetOf<List<Int>>()
    val nearbyTickets = lines[2].split("\n")
    for (i in 1 until nearbyTickets.size) {
        val nums = nearbyTickets[i].split(",").map { it.toInt() }
        var allValid = true
        nums.forEach {
            if (!isAnyValid(specValues, it)) {
                sum += it
                allValid = false
            }
        }
        if (allValid) {
            validTickets.add(nums)
        }
    }
    println(sum)
    return validTickets
}

private fun parseRanges(lines: List<String>): MutableMap<String, MutableSet<Pair<Int, Int>>> {
    val specs = lines[0].split("\n")
    val specValues = mutableMapOf<String, MutableSet<Pair<Int, Int>>>()
    specs.forEach {
        val cSplit = it.split(": ")
        val name = cSplit[0]
        val nums = cSplit[1].split(" or ")

        val specPairs = specValues.getOrPut(name) { mutableSetOf() }
        nums.forEach { numString ->
            val (left, right) = numString.split("-").map { num -> num.toInt() }
            specPairs.add(left to right)
        }
    }
    return specValues
}

fun isAnyValid(specValues: MutableMap<String, MutableSet<Pair<Int, Int>>>, i: Int): Boolean =
    specValues.any { (_, value) ->
        value.any {
            i in it.first..it.second
        }
    }
