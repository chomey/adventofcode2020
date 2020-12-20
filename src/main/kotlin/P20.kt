import kotlin.system.exitProcess
import kotlin.test.assertEquals

private const val length = 10

fun main() {
    val input = load("P20.txt").joinToString("\n").split("\n\n").map { it.split("\n") }
    val tiles = mutableMapOf<Int, Map<Pair<Int, Int>, Char>>()
    input.forEach {
        val tileNumber = it[0].split(" ")[1].removeSuffix(":").toInt()

        val grid = mutableMapOf<Pair<Int, Int>, Char>()
        for (y in 1 until it.size) {
            for (x in it[y].indices) {
                grid[x to y - 1] = it[y][x]
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
            left += grid[0 to y]
            right += grid[length - 1 to y]
        }

        var top = ""
        var bottom = ""
        for (x in 0 until length) {
            top += grid[x to 0]
            bottom += grid[x to length - 1]
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

    val maxX = cutBorders.keys.map { it.first }.max()!!
    repeat(4) {
        cutBorders = cutBorders.mapKeys { it.key.second to maxX - it.key.first - 1 }.toMutableMap()
        hasSeaMonster(cutBorders)
    }
    cutBorders = cutBorders.mapKeys { maxX - it.key.first - 1 to it.key.second }.toMutableMap()
    repeat(4) {
        cutBorders = cutBorders.mapKeys { it.key.second to maxX - it.key.first - 1 }.toMutableMap()
        hasSeaMonster(cutBorders)
    }
}

fun cutBorders(putTogether: MutableMap<Pair<Int, Int>, Char>): MutableMap<Pair<Int, Int>, Char> {
    val result = mutableMapOf<Pair<Int, Int>, Char>()
    val maxX = putTogether.keys.map { it.first }.max()!!
    val maxY = putTogether.keys.map { it.second }.max()!!
    val tempResult = mutableListOf<List<Char>>()
    Outer@ for (y in 0 until maxY) {
        val row = mutableListOf<Char>()
        when (y % 10) {
            0, 9 -> continue@Outer
            else -> Unit
        }
        Inner@ for (x in 0 until maxX) {
            when (x % 10) {
                0, 9 -> continue@Inner
                else -> row.add(putTogether[x to y]!!)
            }
        }
        tempResult.add(row)
    }
    tempResult.indices.forEach { row ->
        tempResult[row].indices.forEach { column ->
            result[column to row] = tempResult[row][column]
        }
    }
    return result
}

fun putTogether(
    tiles: MutableMap<Int, Map<Pair<Int, Int>, Char>>,
    tileSides: MutableMap<Int, MutableMap<String, String>>,
    corners: Set<Int>
): MutableMap<Pair<Int, Int>, Char> {
    lateinit var curr: Pair<Int, Map<Pair<Int, Int>, Char>>
    val result = mutableMapOf<Pair<Int, Int>, Char>()
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

            added.add(curr.first)
            addToMapRight(result, curr.second, xCounter == 0)
        }
    }
    return result
}

fun getFirstSquare(
    tiles: MutableMap<Int, Map<Pair<Int, Int>, Char>>,
    tileSides: MutableMap<Int, MutableMap<String, String>>,
    corners: Set<Int>
): Pair<Int, Map<Pair<Int, Int>, Char>> {
    val topLeft = corners.find { thisTile ->
        val itTileSides = tileSides[thisTile]!!
        val allSides = tileSides.filter { it.key != thisTile }.values.flatMap { it.values }
        !allSides.contains(itTileSides["l"]) && !allSides.contains(itTileSides["t"])
    }!!
    return topLeft to tiles[topLeft]!!
}

fun findNextDown(
    tiles: MutableMap<Int, Map<Pair<Int, Int>, Char>>,
    tileSides: MutableMap<Int, MutableMap<String, String>>,
    result: MutableMap<Pair<Int, Int>, Char>,
    added: Set<Int>
): Pair<Int, Map<Pair<Int, Int>, Char>>? {
    val maxY = result.keys.maxBy { it.second }!!.second
    var top = ""
    for (x in 0 until length) {
        top += result[x to maxY]
    }

    val (downTileNumber, sides) = tileSides.entries.find { (tileNumber, sides) ->
        !added.contains(tileNumber) && sides.containsValue(top)
    } ?: return null

    val side = sides.entries.find { top == it.value }!!
    val baseTiles = tiles[downTileNumber]!!
    val transformed = when (side.key) {
        "t" -> baseTiles.mapKeys { it.key.first to it.key.second }
        "tr" -> baseTiles.mapKeys { length - it.key.first - 1 to it.key.second }
        "l" -> baseTiles.mapKeys { it.key.second to it.key.first }
        "lr" -> baseTiles.mapKeys { length - it.key.second - 1 to it.key.first }
        "r" -> baseTiles.mapKeys { it.key.second to length - it.key.first - 1 }
        "rr" -> baseTiles.mapKeys { length - it.key.second - 1 to length - it.key.first - 1 }
        "br" -> baseTiles.mapKeys { length - it.key.first - 1 to length - it.key.second - 1 }
        else -> error("Implement down: ${side.key}")
    }

    var topTest = ""
    for (x in 0 until length) {
        topTest += transformed[x to 0]
    }

    assertEquals(top, topTest)
    return downTileNumber to transformed
}

fun findNextRight(
    tiles: MutableMap<Int, Map<Pair<Int, Int>, Char>>,
    tileSides: MutableMap<Int, MutableMap<String, String>>,
    added: Set<Int>,
    curr: Pair<Int, Map<Pair<Int, Int>, Char>>
): Pair<Int, Map<Pair<Int, Int>, Char>>? {
    var right = ""
    for (y in 0 until length) {
        right += curr.second[length - 1 to y]
    }

    val (rightTileNumber, sides) = tileSides.entries.find { (tileNumber, sides) ->
        !added.contains(tileNumber) && sides.containsValue(right)
    } ?: return null
    val side = sides.entries.find { right == it.value }!!
    val baseTiles = tiles[rightTileNumber]!!
    val transformed = when (side.key) {
        "l" -> baseTiles.mapKeys { it.key.first to it.key.second }
        "lr" -> baseTiles.mapKeys { it.key.first to length - it.key.second - 1 }
        "b" -> baseTiles.mapKeys { length - it.key.second - 1 to it.key.first }
        "br" -> baseTiles.mapKeys { length - it.key.second - 1 to length - it.key.first - 1 }
        "t" -> baseTiles.mapKeys { it.key.second to it.key.first }
        "tr" -> baseTiles.mapKeys { it.key.second to length - it.key.first - 1 }
        "r" -> baseTiles.mapKeys { length - it.key.first - 1 to it.key.second }
        "rr" -> baseTiles.mapKeys { length - it.key.first - 1 to length - it.key.second - 1 }
        else -> error("Implement right: ${side.key}")
    }

    var rightTest = ""
    for (y in 0 until length) {
        rightTest += transformed[0 to y]
    }

    assertEquals(right, rightTest)
    return rightTileNumber to transformed
}

fun addToMapRight(
    result: MutableMap<Pair<Int, Int>, Char>,
    toAdd: Map<Pair<Int, Int>, Char>,
    newLine: Boolean
) {
    val yMax = result.keys.map { it.second }.max()
    val (xStart, yStart) = when {
        result.isEmpty() -> 0 to 0
        newLine -> {
            0 to yMax!! + 1
        }
        else -> {
            val xMaxOnMaxYLine = result.keys.filter { yMax == it.second }.map { it.first }.max()!!
            xMaxOnMaxYLine + 1 to yMax!! - length + 1
        }
    }
    for (y in 0 until length) {
        for (x in 0 until length) {
            val newPoint = x + xStart to y + yStart
            val oldPoint = x to y
            result[newPoint] = toAdd[oldPoint]!!
        }
    }
//    printGrid(result)
}

fun printGrid(grid: Map<Pair<Int, Int>, Char>) {
    println("Size: ${grid.size}")
    for (y in 0 until 2000) {
        var foundRow = false
        for (x in 0 until 2000) {
            val char = grid[x to y]
            if (char == null) {
                continue
            }
            foundRow = true
            print(char)
        }
        if (foundRow) {
            println()
        }
    }
}

fun hasSeaMonster(grid: Map<Pair<Int, Int>, Char>) {
    val seaMonster = """ 
                  #
#    ##    ##    ###
 #  #  #  #  #  #   """.trimIndent().split("\n")

    val mark = mutableSetOf<Pair<Int, Int>>()
    seaMonster.indices.forEach { y ->
        seaMonster[y].indices.forEach { x ->
            if (seaMonster[y][x] == '#') {
                mark.add(x to y)
            }
        }
    }
    val maxX = grid.keys.map { it.first }.max()!!
    val maxY = grid.keys.map { it.second }.max()!!

    var sum = 0
    for (x in 0 until maxX) {
        for (y in 0 until maxY) {
            if (mark.all { (xMark, yMark) ->
                    grid[x + xMark to y + yMark] == '#'
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