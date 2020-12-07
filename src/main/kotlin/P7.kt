fun main() {
    val lines: List<String> = load("P7.txt")
    val bagsMap = mutableMapOf<String, MutableSet<String>>()
    lines.forEach {
        val parts = it.replace("bags", "bag").split(" contain ")
        val bag = parts[0].replace(" bag", "")

        parts[1].substring(0, parts[1].length - 1).split(",").map { subBag ->
            val bagParts = subBag.trim().split(" ")
            bagParts.subList(1, bagParts.size -1).joinToString(" ")
        }.forEach { subBag ->
            bagsMap.getOrPut(subBag) { mutableSetOf() }.add(bag)
        }
    }

    println(bagsMap)

    val toTry = mutableSetOf("shiny gold")
    val tried = mutableSetOf<String>()
    val result = mutableSetOf<String>()

    while(true){
        val iter = toTry.iterator()
        if(!iter.hasNext()){
            break
        }
        val next = iter.next()
        iter.remove()
        tried.add(next)

        bagsMap[next]?.forEach {
            result.add(it)
            if(!tried.contains(it)){
                toTry.add(it)
            }
        }
    }

    println(result.size)
}
