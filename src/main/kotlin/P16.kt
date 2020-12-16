fun main() {
    val lines = load("P16.txt").joinToString("\n").split("\n\n")

    val specValues = parseRanges(lines)
    val validTickets = sortInvalidTickets(lines, specValues)

    val myTicket = lines[1].split("\n")[1].split(",").map { it.toInt() }
    validTickets.add(myTicket)

    val possibleCategories = computePossibleCategories(myTicket, specValues, validTickets)
    topologicalSortPossible(possibleCategories)

    println(computeProduct(possibleCategories, myTicket))
}

private fun computeProduct(
    possibleCategories: MutableList<MutableSet<String>>,
    myTicket: List<Int>
): Long {
    var product = 1L
    for (i in possibleCategories.indices) {
        val category = possibleCategories[i].first()
        if (category.startsWith("departure")) {
            product *= myTicket[i]
        }
    }
    return product
}

private fun topologicalSortPossible(possibleCategories: MutableList<MutableSet<String>>) {
    while (possibleCategories.any { it.size != 1 }) {
        for (i in possibleCategories.indices) {
            val categorySet = possibleCategories[i]
            if (categorySet.size == 1) {
                val lockedIn = categorySet.first()
                for (j in possibleCategories.indices) {
                    if (i == j) {
                        continue
                    }
                    possibleCategories[j].remove(lockedIn)
                }
            }
        }
    }
}

private fun computePossibleCategories(
    myTicket: List<Int>,
    specValues: MutableMap<String, MutableSet<Pair<Int, Int>>>,
    validTickets: MutableSet<List<Int>>
): MutableList<MutableSet<String>> {
    val possibleCategories = mutableListOf<MutableSet<String>>()
    repeat(myTicket.size) { possibleCategories.add(specValues.keys.toMutableSet()) }

    possibleCategories.indices.forEach {
        val possibleForThis = possibleCategories[it]
        val iter = possibleForThis.iterator()
        while (iter.hasNext()) {
            val curr = iter.next()
            val ranges = specValues[curr]!!
            val isValid = validTickets.all { validTicket ->
                val ticketValue = validTicket[it]
                ranges.any { (min, max) ->
                    ticketValue in min..max
                }
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
        val numbersOnTicket = nearbyTickets[i].split(",").map { it.toInt() }
        var allValid = true
        numbersOnTicket.forEach {
            if (!isAnyValid(specValues, it)) {
                sum += it
                allValid = false
            }
        }
        if (allValid) {
            validTickets.add(numbersOnTicket)
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
            i >= it.first && i <= it.second
        }
    }
