fun main() {
    val loadedInstructions: List<Pair<String, Int>> = loadInstructions("P08.txt")
    val machine = Machine()

    val seen = mutableSetOf(0)
    machine.run(loadedInstructions) {
        return@run if (seen.contains(machine.nextInstruction)) {
            println(machine.acc)
            true
        } else {
            seen.add(machine.nextInstruction)
            false
        }
    }
}
