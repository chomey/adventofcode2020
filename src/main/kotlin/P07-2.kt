import java.lang.Exception

fun main() {
    val lines: List<String> = load("P07.txt")
    val bagsMap = mutableMapOf<String, MutableMap<String, Int>>()
    lines.forEach {
        val parts = it.replace("bags", "bag").split(" contain ")
        val bag = parts[0].replace(" bag", "")

        parts[1].substring(0, parts[1].length - 1).split(",").map { subBag ->
            subBag.trim().split(" ")
        }.forEach { subBagParts ->
            val num = subBagParts[0]
            val subBag = subBagParts.subList(1, subBagParts.size-1).joinToString(separator = " ")
            try {
                bagsMap.getOrPut(bag) { mutableMapOf() }[subBag] = num.toInt()
            } catch (e: Exception){
                // not contains bag, don't care
            }
        }
    }

    println(bagsMap)

    val toTry = mutableMapOf("shiny gold" to 1)

    var result = 0
    while(true){
        val iter = toTry.iterator()
        if(!iter.hasNext()){
            break
        }
        val (next, num) = iter.next()
        result += num
        iter.remove()

        bagsMap[next]?.forEach { (subBag, count) ->
            val currSubBag = toTry.getOrPut(subBag){0} + (num * count)
            toTry[subBag] = currSubBag
        }
    }

    println(result -1)
}