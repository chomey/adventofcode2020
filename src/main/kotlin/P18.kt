fun main() {
    val lines = load("P18.txt")

    var sum = 0L
    var sum2 = 0L
    lines.forEach {
        val scrubbed = it.replace(" ", "")
        sum += compute1(scrubbed)
        sum2 += compute2(scrubbed)
    }
    println(sum)
    println(sum2)
}

enum class Operation {
    NONE, ADD, MULTIPLY
}

private fun compute1(input: String): Long {
    var counter = 0
    var result = 0L
    var parens = 0
    var left: Int
    var operation: Operation = Operation.NONE
    while (counter < input.length) {
        when (val c = input[counter]) {
            '+' -> operation = Operation.ADD
            '*' -> operation = Operation.MULTIPLY
            '(' -> {
                left = counter + 1
                parens++
                while (parens > 0) {
                    when (input[++counter]) {
                        '(' -> parens++
                        ')' -> parens--
                    }
                }
                result = invoke(operation, result, compute1(input.substring(left, counter)))
            }
            else -> result = invoke(operation, result, ("" + c).toLong())
        }
        counter++
    }
    return result
}


private fun compute2(input: String): Long {
    var replaced = ""
    var counter = 0
    var parens = 0
    var left: Int
    while (counter < input.length) {
        when (val c = input[counter]) {
            '(' -> {
                left = counter + 1
                parens++
                while (parens > 0) {
                    when (input[++counter]) {
                        '(' -> parens++
                        ')' -> parens--
                    }
                }
                replaced += compute2(input.substring(left, counter))
            }
            else -> replaced += c
        }
        counter++
    }

    return evaluateABeforeM(replaced)
}

fun evaluateABeforeM(input: String): Long {
    var tokenized = mutableListOf<String>()
    var subString = ""
    for (c in input) {
        when (c) {
            '+', '*' -> {
                tokenized.add(subString)
                tokenized.add("" + c)
                subString = ""
            }
            else -> subString += c
        }
    }
    tokenized.add(subString)

    mutableListOf(
        "+" to fun(a: Long, b: Long): Long { return a + b },
        "*" to fun(a: Long, b: Long): Long { return a * b }
    ).forEach { (token, functor) ->
        while (tokenized.contains(token)) {
            for (i in 1 until tokenized.size - 1) {
                if (tokenized[i] == token) {
                    val left = tokenized.subList(0, i - 1)
                    val middle = functor.invoke(tokenized[i - 1].toLong(), tokenized[i + 1].toLong())
                    val right = tokenized.subList(i + 2, tokenized.size)
                    tokenized = (left + listOf(middle.toString()) + right) as MutableList<String>
                    break
                }
            }
        }
    }
    return tokenized[0].toLong()
}

private fun invoke(operation: Operation, result: Long, value: Long): Long = when (operation) {
    Operation.NONE -> value
    Operation.ADD -> result + value
    Operation.MULTIPLY -> result * value
}

