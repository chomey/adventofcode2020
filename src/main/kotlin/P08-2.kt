fun main() {
    val loadedInstructions: List<Pair<String, Int>> = loadInstructions("P08.txt")

    var curr = 0
    while (curr < loadedInstructions.size - 1) {
        val mutatedInstructions = loadedInstructions.toMutableList()
        val thisCurr = curr++
        val currInstruction = mutatedInstructions[thisCurr]
        when (currInstruction.first) {
            "jmp" -> mutatedInstructions[thisCurr] = "nop" to currInstruction.second
            "nop" -> mutatedInstructions[thisCurr] = "jmp" to currInstruction.second
            else -> continue
        }
        val machine = Machine()
        val seen = mutableSetOf(0)
        machine.run(mutatedInstructions) {
            if(machine.nextInstruction >= loadedInstructions.size){
                machine.success = true
                return@run true
            }
            return@run if (seen.contains(machine.nextInstruction)) {
                true
            } else {
                seen.add(machine.nextInstruction)
                false
            }
        }
        if (machine.success) {
            println(machine.acc)
            return
        }
    }
}
