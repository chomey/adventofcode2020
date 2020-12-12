import kotlin.math.abs

fun main() {
    val lines = load("P12.txt")

    var point = Point(0, 0)

    var currDir = 0

    lines.forEach {
        val inst = it[0]
        val mag = it.substring(1).toInt()

        when (inst) {
            'N' -> point += Point.NORTH.times(mag)
            'S' -> point += Point.SOUTH.times(mag)
            'E' -> point += Point.EAST.times(mag)
            'W' -> point += Point.WEST.times(mag)
            'L' -> {
                currDir += mag % 360
                while (currDir >= 360) {
                    currDir -= 360
                }
            }
            'R' -> {
                currDir -= mag % 360
                while (currDir < 0) {
                    currDir += 360
                }
            }
            'F' -> when (currDir) {
                0 -> point += Point.EAST.times(mag)
                90 -> point += Point.NORTH.times(mag)
                180 -> point += Point.WEST.times(mag)
                270 -> point += Point.SOUTH.times(mag)
            }
        }

        println("$it: ${dir(currDir)}: $point")
    }
    println(abs(point.x) + abs(point.y))
}

fun dir(i: Int) = when(i) {
    0 -> 'E'
    90 -> 'N'
    180 -> 'W'
    270 -> 'S'
    else -> error("Bad input: $i")
}