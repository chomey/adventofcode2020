fun main() {
    val lines = loadGrid("P17.txt")

    val grid = mutableSetOf<NDPoint>()
    val grid2 = mutableSetOf<NDPoint>()
    for (y in lines.indices) {
        for (x in lines[0].indices) {
            when (lines[x][y]) {
                '#' -> {
                    grid.add(NDPoint(x, y, 0))
                    grid2.add(NDPoint(x, y, 0, 0))
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

fun part1(grid: MutableSet<NDPoint>) {
    val lastGrid = grid.toSet()

    val toEvaluate = mutableSetOf<NDPoint>()
    grid.forEach {
        for (i in -1..1) {
            for (j in -1..1) {
                for (k in -1..1) {
                    toEvaluate.add(NDPoint(it.x + i, it.y + j, it.z + k))
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
                    if (lastGrid.contains(NDPoint(it.x + i, it.y + j, it.z + k))) {
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

private fun part2(grid: MutableSet<NDPoint>) {
    val lastGrid = grid.toSet()

    val toEvaluate = mutableSetOf<NDPoint>()
    grid.forEach {
        for (i in -1..1) {
            for (j in -1..1) {
                for (k in -1..1) {
                    for (l in -1..1) {
                        toEvaluate.add(NDPoint(it.x + i, it.y + j, it.z + k, it.w + l))
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
                        if (lastGrid.contains(NDPoint(it.x + i, it.y + j, it.z + k, it.w + l))) {
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

