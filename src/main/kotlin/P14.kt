import java.math.BigInteger
import kotlin.math.pow

fun main() {
    val lines = load("P14.txt")
    one(lines)

    var mask = ""
    val memory = mutableMapOf<String, String>()
    for (i in 0 until lines.size) {
        if (lines[i].startsWith("mask")) {
            mask = lines[i].split(" = ")[1]
        } else {
            val parts = lines[i].split(" = ")
            val preParts = parts[0].split("[")
            val binary = parts[1]
            val index = preParts[1].replace("]", "")
            val indexBinary = Integer.toBinaryString(index.toInt()).padStart(mask.length, '0')
            var toWrite = ""
            for (j in 0 until mask.length) {
                toWrite += when (mask[j]) {
                    '0' -> indexBinary[j]
                    else -> mask[j]
                }
            }
            val wilds = toWrite.count { it == 'X' }
            val wildsArray = 2.0.pow(wilds.toDouble()).toInt()
            for (j in 0 until wildsArray) {
                val wildBinary = Integer.toBinaryString(j).padStart(Integer.toBinaryString(wildsArray).length - 1, '0')

                var xCounter = 0
                var toWriteCopy = ""
                toWrite.indices.forEach {
                    toWriteCopy += when (toWrite[it]) {
                        'X' -> {
                            if (wildBinary[xCounter++] == '0') {
                                '0'
                            } else {
                                '1'
                            }
                        }
                        else -> toWrite[it]
                    }
                }
                memory[toWriteCopy] = binary
            }
        }
    }
    println(memory.values.sumByDouble { it.toDouble() }.toLong())
}

private fun one(lines: List<String>) {
    var mask = ""
    val memory = mutableMapOf<Int, String>()
    for (i in 0 until lines.size) {
        if (lines[i].startsWith("mask")) {
            mask = lines[i].split(" = ")[1]
        } else {
            handleMemory(lines, i, mask, memory)
        }
    }

    var bigIntSum = BigInteger("0")
    memory.values.forEach {
        bigIntSum = bigIntSum.plus(BigInteger(it, 2))
    }
    println(bigIntSum)
}

private fun handleMemory(
    lines: List<String>,
    i: Int,
    mask: String,
    memory: MutableMap<Int, String>
) {
    val parts = lines[i].split(" = ")
    val preParts = parts[0].split("[")
    val index = preParts[1].replace("]", "")
    val binary = parts[1]
    val toAdd = Integer.toBinaryString(binary.toInt()).padEnd(mask.length, '0')
    var newVal = ""

    for (j in 0 until mask.length) {
        newVal += when (mask[j]) {
            '0' -> '0'
            '1' -> '1'
            'X' -> toAdd[j]
            else -> error("Invalid char")
        }
    }
    memory[index.toInt()] = newVal
}

