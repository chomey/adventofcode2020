fun main() {
    val input = load("P21.txt")
    val scrambled = mutableMapOf<String, String?>()
    val possibleTranslations = mutableMapOf<String, MutableSet<MutableSet<String>>>()
    val ingredientCount = mutableMapOf<String, Int>()
    input.forEach {
        val parts = it.split(" (contains ")
        val scrambleds = parts[0].split(" ")
        val english = parts[1].removeSuffix(")").split(", ")
        val scrambledIngredients = scrambleds.toMutableSet()
        scrambledIngredients.forEach { scrambledIngredient ->
            ingredientCount[scrambledIngredient] = ingredientCount.getOrPut(scrambledIngredient) { 0 } + 1
        }

        english.forEach { itEnglish ->
            possibleTranslations.getOrPut(itEnglish) { mutableSetOf() }.add(scrambledIngredients)
            scrambled[itEnglish] = null
        }
    }
    println(possibleTranslations)

    while (true) {
        if (scrambled.values.all { it != null }) {
            break
        }
        possibleTranslations.entries.forEach { (english, possibleSets) ->
            var possible: MutableSet<String> = possibleSets.first()
            for (possibleSet in possibleSets) {
                possible = possible.intersect(possibleSet).toMutableSet()
            }
            possibleTranslations[english] = mutableSetOf(possible)
            if (possible.size == 1) {
                val translated = possible.first()
                scrambled[english] = translated
                possibleTranslations.entries.forEach { (subEnglish, subPossibleSets) ->
                    if (english == subEnglish) {
                        return@forEach
                    }
                    subPossibleSets.forEach { subPossibleSet ->
                        subPossibleSet.remove(translated)
                    }
                }
            }
        }
    }

    println(possibleTranslations)

    val translation = possibleTranslations.mapValues { it.value.first().first() }

    println(ingredientCount.entries.sumBy {
        if (translation.values.contains(it.key)) {
            0
        } else {
            it.value
        }
    })

    println(translation.entries.sortedBy { it.key }.joinToString(",") { it.value })
}
