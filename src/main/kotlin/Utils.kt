@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

import java.io.BufferedReader
import java.io.InputStreamReader

data class Node<T>(val left: Node<T>? = null, val value: T, val right: Node<T>? = null)

data class Point(val coords: List<Double>) {
    constructor(vararg values: Number) : this(values.asList().map { it.toDouble() })

    val x by lazy { coords[0] }
    val y by lazy { coords[1] }
    val z by lazy { coords[2] }
    val w by lazy { coords[3] }

    operator fun plus(other: Point): Point {
        if (coords.size != other.coords.size) {
            error("Invalid sizes: ${coords.size} vs ${other.coords.size}")
        }
        return Point(coords.indices.map { coords[it] + other.coords[it] })
    }

    operator fun times(multiplier: Number): Point = Point(coords.map { it * multiplier.toDouble() })
}

data class Grid<T>(var grid: MutableMap<Point, T> = mutableMapOf()) {
    operator fun get(p: Point): T? = grid[p]
    operator fun get(x: Number, y: Number): T? = grid[Point(x.toDouble(), y.toDouble())]
    operator fun set(p: Point, t: T) {
        grid[p] = t
    }

    operator fun set(x: Number, y: Number, t: T) {
        grid[Point(x.toDouble(), y.toDouble())] = t
    }

    val keys = grid.keys
    val values = grid.values
    val size = grid.size
    fun isEmpty() = grid.isEmpty()
    fun transformKeys(functor: (Point) -> Point): Grid<T> =
        this.also { grid = grid.mapKeys { functor(it.key) }.toMutableMap() }
}

class Machine {
    var nextInstruction = 0
    var acc = 0
    var success = false

    fun run(instructions: List<Pair<String, Int>>, blockAfterCycle: (Machine) -> Boolean) {
        while (true) {
            val (instruction, num) = instructions[nextInstruction]
            when (instruction) {
                "nop" -> nextInstruction++
                "acc" -> {
                    nextInstruction++
                    acc += num
                }
                "jmp" -> nextInstruction += num
                else -> error("Unknown instruction: $instruction $num")
            }
            val shouldExit = blockAfterCycle(this)
            if (shouldExit) {
                return
            }
        }
    }
}

fun load(filename: String) =
    BufferedReader(InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(filename))).readLines()

fun loadInstructions(filename: String): List<Pair<String, Int>> {
    return load(filename).map {
        val splits = it.split(" ")
        splits[0] to splits[1].toInt()
    }
}

fun loadDoubleSpaced(filename: String) = load(filename).joinToString(separator = " ").split("  ")
fun loadInt(filename: String) = load(filename).map { it.toInt() }
fun loadGrid(filename: String): List<List<Char>> {
    return load(filename).map { line ->
        line.toCharArray().toList()
    }
}

fun <T> permutations(values: List<T>): List<List<T>> {
    if (values.size < 2) {
        return listOf(values)
    }

    val result = mutableListOf<List<T>>()
    values.indices.forEach {
        val value = values[it]
        val remainingValues = values.toMutableList().apply { removeAt(it) }
        val permutationsOfRemaining = permutations(remainingValues)
        permutationsOfRemaining.forEach { permutationOfRemaining ->
            result.add(listOf(value) + permutationOfRemaining)
        }
    }
    return result
}