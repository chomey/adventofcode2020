fun main() {
    val loadedInstructions: List<String> = load("P8.txt")
    var swapping = 0
    Outer@ while (swapping < loadedInstructions.size) {
        val instructions = loadedInstructions.toMutableList()
        val thisInstruction = instructions[swapping]
        if (thisInstruction.startsWith("nop")) {
            instructions[swapping] = instructions[swapping].replace("nop", "jmp")
        } else if (thisInstruction.startsWith("jmp")) {
            instructions[swapping] = instructions[swapping].replace("jmp", "nop")
        }

        var curr = 0
        var acc = 0
        val visited = mutableSetOf<Int>()
        while (true) {
            visited.add(curr)
            val parts = instructions[curr].split(" ")
            val instruction = parts[0]
            val num = parts[1].toInt()

            when (instruction) {
                "nop" -> curr++
                "acc" -> {
                    acc += num
                    curr++
                }
                "jmp" -> curr += num
            }
            if (curr >= instructions.size) {
                println(acc)
                return
            }
            if (visited.contains(curr)) {
                swapping++
                continue@Outer
            }
        }
    }
}
