fun main() {
    val lines: List<String> = load("P5.txt")
    var max = Integer.MIN_VALUE
    val seats = mutableSetOf<Int>()
    lines.forEach {
        val rowString = it.substring(0, 7)
        val row = Integer.parseInt(rowString.replace("F", "0").replace("B", "1"), 2)
        val seatString = it.substring(7, 10)
        val seat = Integer.parseInt(seatString.replace("L", "0").replace("R", "1"), 2)
        val seatId = 8 * row + seat
        seats.add(seatId)
        if (seatId > max) {
            max = seatId
        }
    }
    println(max)
    var i = 1
    while(true){
        if(seats.contains(i-1) && seats.contains(i+1) && !seats.contains(i)){
            println(i)
            return
        }
        i++
    }
}