import kotlin.math.abs

fun main() {
    val lines = load("P12.txt")

    var point = Point(0, 0)

    var currDir = 0

    lines.forEach {
        val inst = it[0]
        val mag = it.substring(1).toInt()

        when (inst) {
            'N' -> point += Point(0, mag)
            'S' -> point += Point(0, -mag)
            'E' -> point += Point(mag, 0)
            'W' -> point += Point(-mag, 0)
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
                0 -> point += Point(mag, 0)
                90 -> point += Point(0, mag)
                180 -> point += Point(-mag, 0)
                270 -> point += Point(0, -mag)
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
