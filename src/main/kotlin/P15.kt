fun main() {
    val input = "1,20,8,12,0,14"
    val vals = input.split(",").map { it.toInt() }

    var counter = 0
    val lastSaidTurns = HashMap<Int, Int>(1000000)
    var lastSaid = 0
    while (counter < 30000000) {
        if (counter < vals.size) {
            if (counter > 0) {
                lastSaidTurns[lastSaid] = counter - 1
            }
            lastSaid = vals[counter]
        } else {
            val lastSaidTurn = lastSaidTurns[lastSaid]
            if (lastSaidTurn == null) {
                lastSaidTurns[lastSaid] = counter - 1
                lastSaid = 0
            } else {
                lastSaidTurns[lastSaid] = counter - 1
                lastSaid = counter - 1 - lastSaidTurn
            }
        }
        counter++
    }
    println(lastSaid)
}