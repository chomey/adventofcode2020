fun main() {
    val input = load("P24.txt")
    val points = mutableSetOf<Point>()
    // x = -1W, 1E
    // y = -1SW, 1NE
    // z = -1NW, 1SE

    val reference = Point(0, 0)
    val EAST = Point(1, 0)
    val NORTH_EAST = Point(.5, 1)
    val SOUTH_EAST = Point(.5, -1)
    val WEST = Point(-1, 0)
    val NORTH_WEST = Point(-.5, 1)
    val SOUTH_WEST = Point(-.5, -1)
    val ALL_DIRS = listOf(EAST, NORTH_EAST, SOUTH_EAST, WEST, NORTH_WEST, SOUTH_WEST)

    input.forEach { sequence ->
        var curr = reference.copy()
        var i = 0
        while (i < sequence.length) {
            val dir: String = when (val c = sequence[i++]) {
                'e', 'w' -> "$c"
                else -> "$c${sequence[i++]}"
            }
            val point = when (dir) {
                "e" -> EAST
                "ne" -> NORTH_EAST
                "se" -> SOUTH_EAST
                "w" -> WEST
                "nw" -> NORTH_WEST
                "sw" -> SOUTH_WEST
                else -> error("Invalid dir: $dir")
            }
            curr += point
        }
        if (points.contains(curr)) {
            points.remove(curr)
        } else {
            points.add(curr)
        }
    }
    println(points.size)

    var dayStart = points.toSet()
    val days = 100
    for (i in 0 until days) {
        val toEvaluate = mutableSetOf<Point>()
        dayStart.forEach {
            ALL_DIRS.forEach { dir ->
                toEvaluate.add(it + dir)
            }
        }

        val tomorrowStart = mutableSetOf<Point>()
        toEvaluate.forEach { point ->
            val wasBlack = dayStart.contains(point)
            val adjacent = ALL_DIRS.count { dayStart.contains(point + it) }
            when {
                wasBlack -> {
                    when (adjacent) {
                        1, 2 -> tomorrowStart.add(point)
                    }
                }
                else -> {
                    if(adjacent == 2){
                        tomorrowStart.add(point)
                    }
                }
            }
        }
        dayStart = tomorrowStart
    }
    println(dayStart.size)
}

