fun main() {
    fun bounds(value: Int, min: Int, max: Int) {
        if (value < min) {
            error("min: $value $min")
        }
        if (value > max) {
            error("max: $value $max")
        }
    }

    val records = load("P4.txt").joinToString(separator = " ").split("  ")
    val required = mapOf(
        "byr" to fun(s: String) { bounds(s.toInt(), 1920, 2002) },
        "iyr" to fun(s: String) { bounds(s.toInt(), 2010, 2020) },
        "eyr" to fun(s: String) { bounds(s.toInt(), 2020, 2030) },
        "hgt" to fun(s: String) {
            val hgt = s.substring(0, s.length - 2).toInt()
            if (s.endsWith("cm")) {
                bounds(hgt, 150, 193)
            } else if (s.endsWith("in")) {
                bounds(hgt, 59, 76)
            }
        },
        "hcl" to fun(s: String) {
            if (!Regex("#[0-9a-f]{6}").matches(s)) {
                error("not matches: $s")
            }
        },
        "ecl" to fun(s: String) {
            if (!setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(s)) {
                error("cl: $s")
            }
        },
        "pid" to fun(s: String) {
            if (s.length != 9) {
                error("length")
            }
        },
    )

    var valid = 0
    records.forEach {
        val parts = it.split(" ")
        try {
            val keys = mutableSetOf<String>()
            parts.forEach { part ->
                val (key, value) = part.split(":")
                required[key]?.invoke(value)
                keys.add(key)
            }
            if(keys.containsAll(required.keys)) {
                valid++
            }
        } catch (e: Exception) {
//            println(e)
        }
    }
    println(valid)
}