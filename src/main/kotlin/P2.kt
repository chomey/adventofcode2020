fun main() {
    val values = load( "P2.txt")
    var result = 0
    values.forEach {
        val splits = it.split(" ")
        val nums = splits[0].split("-")
        val left = nums[0].toInt()
        val right = nums[1].toInt()
        val c = splits[1].first()


        val b = splits[2][left - 1] == c
        val b1 = splits[2][right - 1] == c
        if ((b&&!b1) || (b1&&!b)) {
            result++
        }
    }
    println(result)
}