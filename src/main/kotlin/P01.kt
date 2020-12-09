fun main() {
    val values = loadInt("P01.txt")
    values.forEach { a ->
        values.forEach { b ->
            if (a + b == 2020) {
                println("1: ${a * b}")
            }
            values.forEach { c ->
                if (a + b + c == 2020) {
                    println("2: ${a * b * c}")
                }
            }
        }
    }
}