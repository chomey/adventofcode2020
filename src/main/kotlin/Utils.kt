@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

import java.io.BufferedReader
import java.io.File
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

fun load(filename: String): List<String> =
    BufferedReader(InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(filename))).readLines()
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