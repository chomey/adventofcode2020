@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

import java.io.BufferedReader
import java.io.InputStreamReader

data class Node<T>(val left: Node<T>? = null, val value: T, val right: Node<T>? = null)

class Point(val x: Int, val y: Int) {
    companion object {
        val LEFT = Point(-1, 0)
        val RIGHT = Point(1, 0)
        val UP = Point(0, -1)
        val DOWN = Point(0, -1)
        val NORTH = UP
        val SOUTH = DOWN
        val EAST = RIGHT
        val WEST = LEFT
    }
}

operator fun Point.plus(other: Point) = Point(x + other.x, y + other.y)
operator fun Point.times(multiplier: Int) = Point(x * multiplier, y * multiplier)

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
    val results = mutableListOf<List<T>>()
    if (values.isEmpty()) {
        return listOf()
    }

    values.indices.forEach {
        val value = values[it]
        val remainingValues = values.toMutableList().apply { removeAt(it) }
        val permutationsOfRemaining = permutations(remainingValues)
        val newResult = mutableListOf(value)
        if (permutationsOfRemaining.isEmpty()) {
            results.add(newResult)
        } else {
            permutationsOfRemaining.forEach { permutation ->
                newResult.addAll(permutation)
                results.add(newResult.toMutableList().apply { addAll(permutation) })
            }
        }
    }
    return results
}