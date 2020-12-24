import kotlin.system.exitProcess
import kotlin.test.assertEquals

private const val length = 10

fun main() {
    val input = load("P20.txt").joinToString("\n").split("\n\n").map { it.split("\n") }
    val tiles = mutableMapOf<Int, Grid<Char>>()
    input.forEach {
        val tileNumber = it[0].split(" ")[1].removeSuffix(":").toInt()

        val grid = Grid<Char>()
        for (y in 1 until it.size) {
            for (x in it[y].indices) {
                grid[x, y -1]= it[y][x]
            }
        }
        tiles[tileNumber] = grid
    }

    val tileSides = mutableMapOf<Int, MutableMap<String, String>>()
    tiles.forEach { (tileNumber, grid) ->
        val sides = mutableMapOf<String, String>()
        var left = ""
        var right = ""
        for (y in 0 until length) {
            left += grid[0, y]
            right += grid[length - 1, y]
        }

        var top = ""
        var bottom = ""
        for (x in 0 until length) {
            top += grid[x, 0]
            bottom += grid[x, length - 1]
        }
        sides["l"] = left
        sides["lr"] = left.reversed()
        sides["r"] = right
        sides["rr"] = right.reversed()
        sides["t"] = top
        sides["tr"] = top.reversed()
        sides["b"] = bottom
        sides["br"] = bottom.reversed()
        tileSides[tileNumber] = sides
    }

    val adjacent = mutableMapOf<Int, MutableSet<Int>>()
    tileSides.entries.forEach { (tileNumber, sides) ->
        tileSides.entries.forEach Inner@{ (otherTileNumber, otherSides) ->
            if (tileNumber == otherTileNumber) {
                return@Inner
            }
            val intersect = sides.values.intersect(otherSides.values)
            if (intersect.isNotEmpty()) {
                adjacent.getOrPut(tileNumber) { mutableSetOf() }.add(otherTileNumber)
            }
        }
    }

    var product = 1L
    val corners = adjacent.filter { it.value.size == 2 }
    corners.keys.forEach { product *= it }

    val putTogether = putTogether(tiles, tileSides, corners.keys)

    var cutBorders = cutBorders(putTogether)

    val maxX = cutBorders.keys.map { it.x }.maxOrNull()!!
    repeat(4) {
        cutBorders = cutBorders.transformKeys { Point(it.y, maxX - it.x - 1) }
        hasSeaMonster(cutBorders)
    }
    cutBorders = cutBorders.transformKeys { Point(maxX - it.x - 1, it.y )}
    repeat(4) {
        cutBorders = cutBorders.transformKeys { Point(it.y, maxX - it.x - 1 )}
        hasSeaMonster(cutBorders)
    }
}

fun cutBorders(putTogether: Grid<Char>): Grid<Char> {
    val result = Grid<Char>()
    val maxX = putTogether.keys.map { it.x }.maxOrNull()!!
    val maxY = putTogether.keys.map { it.y }.maxOrNull()!!
    val tempResult = mutableListOf<List<Char>>()
    Outer@ for (y in 0 until maxY.toInt()) {
        val row = mutableListOf<Char>()
        when (y % 10) {
            0, 9 -> continue@Outer
            else -> Unit
        }
        Inner@ for (x in 0 until maxX.toInt()) {
            when (x % 10) {
                0, 9 -> continue@Inner
                else -> row.add(putTogether[x, y]!!)
            }
        }
        tempResult.add(row)
    }
    tempResult.indices.forEach { row ->
        tempResult[row].indices.forEach { column ->
            result[column, row] = tempResult[row][column]
        }
    }
    return result
}

fun putTogether(
    tiles: MutableMap<Int, Grid<Char>>,
    tileSides: MutableMap<Int, MutableMap<String, String>>,
    corners: Set<Int>
): Grid<Char> {
    lateinit var curr: Pair<Int, Grid<Char>>
    val result = Grid<Char>()
    val added = mutableSetOf<Int>()
    for (yCounter in 0 until Integer.MAX_VALUE) {
        curr = if (yCounter == 0) {
            getFirstSquare(tiles, tileSides, corners)
        } else {
            findNextDown(tiles, tileSides, result, added) ?: break
        }
        for (xCounter in 0 until Integer.MAX_VALUE) {
            curr = if (xCounter == 0) {
                curr
            } else {
                findNextRight(tiles, tileSides, added, curr) ?: break
            }

            val (tileNumber, grid) = curr

            added.add(tileNumber)
            addToMapRight(result, grid, xCounter == 0)
        }
    }
    return result
}

fun getFirstSquare(
    tiles: MutableMap<Int, Grid<Char>>,
    tileSides: MutableMap<Int, MutableMap<String, String>>,
    corners: Set<Int>
): Pair<Int, Grid<Char>> {
    val topLeft = corners.find { thisTile ->
        val itTileSides = tileSides[thisTile]!!
        val allSides = tileSides.filter { it.key != thisTile }.values.flatMap { it.values }
        !allSides.contains(itTileSides["l"]) && !allSides.contains(itTileSides["t"])
    }!!
    return topLeft to tiles[topLeft]!!
}

fun findNextDown(
    tiles: MutableMap<Int, Grid<Char>>,
    tileSides: MutableMap<Int, MutableMap<String, String>>,
    result: Grid<Char>,
    added: Set<Int>
): Pair<Int, Grid<Char>>? {
    val maxY = result.keys.map { it.y }.maxOrNull()!!
    var top = ""
    for (x in 0 until length) {
        top += result[x, maxY]
    }

    val (downTileNumber, sides) = tileSides.entries.find { (tileNumber, sides) ->
        !added.contains(tileNumber) && sides.containsValue(top)
    } ?: return null

    val side = sides.entries.find { top == it.value }!!
    val baseTiles = tiles[downTileNumber]!!
    val transformed = when (side.key) {
        "t" -> baseTiles.transformKeys { Point(it.x, it.y )}
        "tr" -> baseTiles.transformKeys { Point(length - it.x - 1, it.y )}
        "l" -> baseTiles.transformKeys { Point(it.y, it.x )}
        "lr" -> baseTiles.transformKeys { Point(length - it.y - 1, it.x )}
        "r" -> baseTiles.transformKeys { Point(it.y, length - it.x - 1 )}
        "rr" -> baseTiles.transformKeys { Point(length - it.y - 1, length - it.x - 1 )}
        "br" -> baseTiles.transformKeys { Point(length - it.x - 1, length - it.y - 1 )}
        else -> error("Implement down: ${side.key}")
    }

    var topTest = ""
    for (x in 0 until length) {
        topTest += transformed[x, 0]
    }

    assertEquals(top, topTest)
    return downTileNumber to transformed
}

fun findNextRight(
    tiles: MutableMap<Int, Grid<Char>>,
    tileSides: MutableMap<Int, MutableMap<String, String>>,
    added: Set<Int>,
    curr: Pair<Int, Grid<Char>>
): Pair<Int, Grid<Char>>? {
    var right = ""
    for (y in 0 until length) {
        right += curr.second[length - 1, y]
    }

    val (rightTileNumber, sides) = tileSides.entries.find { (tileNumber, sides) ->
        !added.contains(tileNumber) && sides.containsValue(right)
    } ?: return null
    val side = sides.entries.find { right == it.value }!!
    val baseTiles = tiles[rightTileNumber]!!
    val transformed = when (side.key) {
        "l" -> baseTiles.transformKeys { Point(it.x, it.y) }
        "lr" -> baseTiles.transformKeys { Point(it.x, length - it.y - 1) }
        "b" -> baseTiles.transformKeys { Point(length - it.y - 1, it.x) }
        "br" -> baseTiles.transformKeys { Point(length - it.y - 1, length - it.x - 1) }
        "t" -> baseTiles.transformKeys { Point(it.y, it.x) }
        "tr" -> baseTiles.transformKeys { Point(it.y, length - it.x - 1) }
        "r" -> baseTiles.transformKeys { Point(length - it.x - 1, it.y) }
        "rr" -> baseTiles.transformKeys { Point(length - it.x - 1, length - it.y - 1) }
        else -> error("Implement right: ${side.key}")
    }

    var rightTest = ""
    for (y in 0 until length) {
        rightTest += transformed[0, y]
    }

    assertEquals(right, rightTest)
    return rightTileNumber to transformed
}

fun addToMapRight(
    result: Grid<Char>,
    toAdd: Grid<Char>,
    newLine: Boolean
) {
    val yMax = result.keys.map { it.y }.maxOrNull()
    val (xStart, yStart) = when {
        result.isEmpty() -> 0 to 0
        newLine -> 0 to yMax!! + 1
        else -> {
            val xMaxOnMaxYLine = result.keys.filter { yMax == it.y }.map { it.x }.maxOrNull()!!
            xMaxOnMaxYLine + 1 to yMax!! - length + 1
        }
    }
    for (y in 0 until length) {
        for (x in 0 until length) {
            result[x + xStart.toInt(), y + yStart.toInt()] = toAdd[x, y]!!
        }
    }
    printGrid(result)
}

fun printGrid(grid: Grid<Char>) {
    var skipPrint = false
    skipPrint = true // COMMENT THIS LINE OUT TO PRINT
    if(skipPrint){
        return
    }
    println("Size: ${grid.size}")
    for (y in 0 until 2000) {
        var foundRow = false
        for (x in 0 until 2000) {
            val char = grid[x, y] ?: continue
            foundRow = true
            print(char)
        }
        if (foundRow) {
            println()
        }
    }
}

fun hasSeaMonster(grid: Grid<Char>) {
    val seaMonster = """ 
                  #
#    ##    ##    ###
 #  #  #  #  #  #   """.trimIndent().split("\n")

    val mark = mutableSetOf<Point>()
    seaMonster.indices.forEach { y ->
        seaMonster[y].indices.forEach { x ->
            if (seaMonster[y][x] == '#') {
                mark.add(Point(x, y))
            }
        }
    }
    val maxX = grid.keys.map { it.x }.maxOrNull()!!.toInt()
    val maxY = grid.keys.map { it.y }.maxOrNull()!!.toInt()

    var sum = 0
    for (x in 0 until maxX) {
        for (y in 0 until maxY) {
            if (mark.all { point ->
                    grid[Point(x, y) + point] == '#'
                }) {
                sum++
            }
        }
    }

    if (sum != 0) {
        println(grid.values.count { it == '#' } - sum * seaMonster.sumBy { it.count { char -> char == '#' } })
        exitProcess(0)
    }
}