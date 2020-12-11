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
                var count = 0
                val dirs = listOf(
                    -1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1
                )
                dirs.forEach { (deltaX, deltaY) ->
                    var currY = row
                    var currX = column
                    while (true) {
                        val newY = currY + deltaY
                        if (newY < 0 || newY >= last.size) {
                            break
                        }
                        val newX = currX + deltaX
                        if (newX < 0 || newX >= line.size) {
                            break
                        }
                        when (last[newY][newX]) {
                            '#' -> {
                                count++
                                break
                            }
                            'L' -> break
                        }
                        currX = newX
                        currY = newY
                    }
                }
                when (val oldChar = last[row][column]) {
                    'L' -> {
                        if (count == 0) {
                            newLine.add('#')
                        } else {
                            newLine.add(oldChar)
                        }
                    }
                    '#' -> {
                        if (count >= 5) {
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