fun main() {
    val input = load("P22.txt")
    println(input)

    val players = input.joinToString("\n").split("\n\n")
    val p1 = players[0].split("\n")
    val p1Deck = p1.subList(1, p1.size).map { it.toLong() }.toMutableList()
    val p2 = players[1].split("\n")
    val p2Deck = p2.subList(1, p2.size).map { it.toLong() }.toMutableList()

//    p1(p1Deck.toMutableList(), p2Deck.toMutableList())

    recursiveCombat(p1Deck, p2Deck)

    println(countScore(p1Deck))
    println(countScore(p2Deck))
}

//returns true if p1 wins
private fun recursiveCombat(
    p1Deck: MutableList<Long>,
    p2Deck: MutableList<Long>
): Boolean {
    val previousStates = mutableSetOf<Pair<List<Long>, List<Long>>>()
    while (p1Deck.isNotEmpty() && p2Deck.isNotEmpty()) {
        val p1State = p1Deck.toList()
        val p2State = p2Deck.toList()

        val thisState = p1State to p2State
        if (previousStates.contains(thisState)) {
            return true
        }
        previousStates.add(thisState)

        val topP1 = p1Deck.removeAt(0)
        val topP2 = p2Deck.removeAt(0)

        val p1Recurse = p1Deck.size >= topP1.toInt()
        val p2Recurse = p2Deck.size >= topP2.toInt()

        when {
            p1Recurse && p2Recurse -> {
                when (recursiveCombat(
                    p1Deck.subList(0, topP1.toInt()).toMutableList(),
                    p2Deck.subList(0, topP2.toInt()).toMutableList()
                )) {
                    true -> {
                        p1Deck.add(topP1)
                        p1Deck.add(topP2)
                    }
                    false -> {
                        p2Deck.add(topP2)
                        p2Deck.add(topP1)
                    }
                }
            }
            topP1 == topP2 -> {
                p1Deck.add(topP1)
                p2Deck.add(topP2)
            }
            topP1 > topP2 -> {
                p1Deck.add(topP1)
                p1Deck.add(topP2)
            }
            topP1 < topP2 -> {
                p2Deck.add(topP2)
                p2Deck.add(topP1)
            }
        }
    }
    return p1Deck.isNotEmpty()
}

private fun p1(
    p1Deck: MutableList<Long>,
    p2Deck: MutableList<Long>
) {
    while (p1Deck.isNotEmpty() && p2Deck.isNotEmpty()) {
        val topP1 = p1Deck.removeAt(0)
        val topP2 = p2Deck.removeAt(0)

        when {
            topP1 == topP2 -> {
                p1Deck.add(topP1)
                p2Deck.add(topP2)
            }
            topP1 > topP2 -> {
                p1Deck.add(topP1)
                p1Deck.add(topP2)
            }
            topP1 < topP2 -> {
                p2Deck.add(topP2)
                p2Deck.add(topP1)
            }
        }
    }

    println(countScore(p1Deck))
    println(countScore(p2Deck))
}

private fun countScore(deck: MutableList<Long>): Long {
    var sum = 0L
    for (i in deck.indices) {
        sum += (deck.size - i) * deck[i]
    }
    return sum
}
