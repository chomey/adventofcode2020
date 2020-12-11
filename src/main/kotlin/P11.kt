fun main() {
    val lines = loadGrid("P11.txt")
    printGrid(lines)

    var last = lines.toList()
    while (true) {
        val next = mutableListOf<MutableList<Char>>()
        for (row in 0 until last.size) {
            val line = last[row]
            val newLine = mutableListOf<Char>()
            for (column in line.indices) {
                val counts = mutableMapOf<Char, Int>()
                for (y in -1 until 2) {
                    val newY = y + row
                    if (newY < 0 || newY >= last.size) {
                        continue
                    }
                    for (x in -1 until 2) {
                        val newX = x + column
                        if (newX < 0 || newX >= line.size) {
                            continue
                        }
                        if (x == 0 && y == 0) {
                            continue
                        }
                        val char = last[newY][newX]
                        val lastCount = counts.getOrPut(char) { 0 }
                        counts[char] = lastCount + 1
                    }
                }
                when (val oldChar = last[row][column]) {
                    'L' -> {
                        if (counts.getOrDefault('#', 0) == 0) {
                            newLine.add('#')
                        } else {
                            newLine.add(oldChar)
                        }
                    }
                    '#' -> {
                        if (counts.getOrDefault('#', 0) >= 4) {
                            newLine.add('L')
                        } else {
                            newLine.add('#')
                        }
                    }
                    '.' -> newLine.add('.')
                }
            }
            next.add(newLine)
        }
        if (last == next) {
            println(last.sumBy { it.count { char -> char == '#' } })
            return
        }
        last = next
//        printGrid(last)
    }
}

fun printGrid(grid: List<List<Char>>) {
    grid.forEach {
        it.forEach { char ->
            print(char)
        }
        println()
    }
    println()
}