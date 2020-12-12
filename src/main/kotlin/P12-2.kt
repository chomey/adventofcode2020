fun main() {
    val lines = load("P12.txt")

    var ship = Point(0, 0)
    var waypoint = Point(10, 1)

    // EAST = 0
    // NORTH = 1
    // WEST = 2
    // SOUTH = 3

    lines.forEach {
        val inst = it[0]
        val mag = it.substring(1).toInt()

        when (inst) {
            'N' -> waypoint += Point.NORTH.times(mag)
            'S' -> waypoint += Point.SOUTH.times(mag)
            'E' -> waypoint += Point.EAST.times(mag)
            'W' -> waypoint += Point.WEST.times(mag)
            'L' -> {
                when (mag % 360) {
                    0 -> {
                    }
                    90 -> {
                        waypoint = Point(-waypoint.y, waypoint.x)
                    }
                    180 -> {
                        waypoint = Point(-waypoint.x, -waypoint.y)
                    }
                    270 -> {
                        waypoint = Point(waypoint.y, -waypoint.x)
                    }
                }
            }
            'R' -> {
                when (mag % 360) {
                    0 -> {
                    }
                    90 -> {
                        waypoint = Point(waypoint.y, -waypoint.x)
                    }
                    180 -> {
                        waypoint = Point(-waypoint.x, -waypoint.y)
                    }
                    270 -> {
                        waypoint = Point(-waypoint.y, waypoint.x)
                    }
                }
            }
            'F' -> ship += waypoint.times(mag)
        }
        println(ship)
    }
}