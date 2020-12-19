fun main() {
    val qParts = load("P19.txt").joinToString(separator = "\n").split("\n\n")

    val rules = parseRules(qParts[0])
    val pattern = "(${rules["0"]!!})"
    println(pattern)
    val regex = Regex(pattern)
//    qParts[1].split("\n").forEach {
//        if (regex.matches(it)) {
//            println(it)
//        }
//    }
    println(qParts[1].split("\n").count {
        regex.matches(it)
    })
}

fun parseRules(rules: String): MutableMap<String, String> {
    val result = mutableMapOf<String, String>()
    rules.split("\n").forEach {
        val ruleSplit = it.split(":")
        val key = ruleSplit[0]
        val trimmed = ruleSplit[1].trim()
        val indexOf = trimmed.indexOf("|")
        val cleaned = if (indexOf == -1) {
            trimmed
        } else {
            "( ${trimmed.substring(0, indexOf - 1)} ) | ( ${trimmed.subSequence(indexOf + 2, trimmed.length)} )"
        }
        result[key] = cleaned
    }
    println(result)
    var subRegex = "( 42 31 )"
    repeat(5){
        val repeated = mutableListOf<String>()
        repeat(it + 1){
            repeated.add(0, "42")
            repeated.add("31")
        }
        subRegex = "( $subRegex | ( ${repeated.joinToString(separator = " ")} ) )"
    }
    println(subRegex)
    while (true) {
        val (key, toReplace) = result.entries.find { hasNumbers(it.value) } ?: break
        val parts = toReplace.split(" ")
        val newValue = mutableListOf<String>()
        parts.forEach { part ->
            newValue.add(
                when {
                    part == "|" -> "|"
                    part == "(" -> "("
                    part == ")" -> ")"
                    part == "+" -> "+"
                    part.startsWith("\"") -> part
                    part == "8" -> "( ( 42 ) + )"
                    part == "11" -> subRegex
                    else -> "( ${result[part]!!} )"
                }
            )
        }
        val newJoinedValue = newValue.joinToString(separator = " ")
        result[key] = newJoinedValue
    }
    for (key in result.keys) {
        result[key] = result[key]!!
            .replace(" ", "")
            .replace("\"", "")
    }
    return result
}

fun hasNumbers(rule: String) = Regex(".*[0-9]+.*").matches(rule)