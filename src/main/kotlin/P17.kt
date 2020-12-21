fun main() {
    val lines = loadGrid("P17.txt")

    val grid = mutableSetOf<Point>()
    val grid2 = mutableSetOf<Point>()
    for (y in lines.indices) {
        for (x in lines[0].indices) {
            when (lines[x][y]) {
                '#' -> {
                    grid.add(Point(x, y, 0))
                    grid2.add(Point(x, y, 0, 0))
                }
            }
        }
    }

    val cycles = 6
    repeat(cycles) {
        part1(grid)
        part2(grid2)
    }

    println(grid.size)
    println(grid2.size)
}

fun part1(grid: MutableSet<Point>) {
    val lastGrid = grid.toSet()

    val toEvaluate = mutableSetOf<Point>()
    grid.forEach {
        for (i in -1..1) {
            for (j in -1..1) {
                for (k in -1..1) {
                    toEvaluate.add(Point(it.x + i, it.y + j, it.z + k))
                }
            }
        }
    }
    grid.clear()

    toEvaluate.forEach {
        var active = 0
        for (i in -1..1) {
            for (j in -1..1) {
                for (k in -1..1) {
                    if (i == 0 && j == 0 && k == 0) {
                        continue
                    }
                    if (lastGrid.contains(Point(it.x + i, it.y + j, it.z + k))) {
                        active++
                    }
                }
            }
        }

        when {
            lastGrid.contains(it) -> when (active) {
                2, 3 -> grid.add(it)
            }
            else -> when (active) {
                3 -> grid.add(it)
            }
        }
    }
}

private fun part2(grid: MutableSet<Point>) {
    val lastGrid = grid.toSet()

    val toEvaluate = mutableSetOf<Point>()
    grid.forEach {
        for (i in -1..1) {
            for (j in -1..1) {
                for (k in -1..1) {
                    for (l in -1..1) {
                        toEvaluate.add(Point(it.x + i, it.y + j, it.z + k, it.w + l))
                    }
                }
            }
        }
    }
    grid.clear()

    toEvaluate.forEach {
        var active = 0
        for (i in -1..1) {
            for (j in -1..1) {
                for (k in -1..1) {
                    for (l in -1..1) {
                        if (i == 0 && j == 0 && k == 0 && l == 0) {
                            continue
                        }
                        if (lastGrid.contains(Point(it.x + i, it.y + j, it.z + k, it.w + l))) {
                            active++
                        }
                    }
                }
            }
        }

        when {
            lastGrid.contains(it) -> when (active) {
                2, 3 -> grid.add(it)
            }
            else -> when (active) {
                3 -> grid.add(it)
            }
        }
    }
}

