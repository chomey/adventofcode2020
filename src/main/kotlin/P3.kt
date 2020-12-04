fun func(grid: List<List<Char>>, x: Int, y: Int): Long {
    var count = 0L
    val height = grid.size
    repeat((height - 1) / y) {
        val i = it + 1
        val newx = (x * i) % grid[0].size
        val newy = (y * i)
        if (grid[newy][newx] == '#') {
            count++
        }
    }
    return count
}

fun main() {
    val grid = loadGrid("P3.txt")
    var count = func(grid, 3, 1)
    count *= func(grid, 1, 1)
    count *= func(grid, 5, 1)
    count *= func(grid, 7, 1)
    count *= func(grid, 1, 2)
    println(count)
}
