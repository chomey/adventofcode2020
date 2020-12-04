fun main() {
    val records: List<String> = load("P4.txt")
    val requiredKeys = setOf(
        "byr",
        "iyr",
        "eyr",
        "hgt",
        "hcl",
        "ecl",
        "pid",
    )

    var allReq = 0
    var valid = 0
    records.forEach {
        val parts = it.trim().split(" ")
        val partsMap = mutableMapOf<String, String>()
        parts.forEach {
            val (key, value) = it.split(":")
            if (key != "cid") {
                partsMap[key] = value
            }
        }
        if (partsMap.keys.containsAll(requiredKeys)) {
            allReq++
            try {
                bounds(partsMap["byr"]!!.toInt(), 1920, 2002)
                bounds(partsMap["iyr"]!!.toInt(), 2010, 2020)
                bounds(partsMap["eyr"]!!.toInt(), 2020, 2030)
                val hgtValue = partsMap["hgt"]!!
                val hgt = hgtValue.substring(0, hgtValue.length - 2).toInt()
                if (hgtValue.endsWith("cm")) {
                    bounds(hgt, 150, 193)
                } else if (hgtValue.endsWith("in")) {
                    bounds(hgt, 59, 76)
                }
                if (!Regex("#[0-9a-f]{6}").matches(partsMap["hcl"]!!)) {
                    error("not matches: ${partsMap["hcl"]!!}")
                }
                if (!setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(partsMap["ecl"]!!)) {
                    error("cl: ${partsMap["ecl"]}")
                }
                if (partsMap["pid"]!!.length != 9) {
                    error("length")
                }
                valid++
            } catch (e: Exception) {
//                println(e.message)
            }
        }
    }
    println(allReq)
    println(valid)
}

fun bounds(value: Int, min: Int, max: Int) {
    if (value < min) {
        error("min: $value $min")
    }
    if (value > max) {
        error("max: $value $max")
    }
}
