package day

object Day11 : Day("11", "10605", "2713310158") {
    override fun examplePartOne() = getExampleList().readMonkeys().playTimes(20).solve()
    override fun examplePartTwo() = getExampleList().readMonkeys().playTimes(10_000, true).solve()
    override fun solvePartOne() = getInputList().readMonkeys().playTimes(20).solve()
    override fun solvePartTwo() = getInputList().readMonkeys().playTimes(10_000, true).solve()

    private fun List<String>.readMonkeys() = chunked(7).map(::readMonkey)

    private fun readMonkey(input: List<String>): Monkey {
        val items = input[1].split("""\D+""".toRegex()).filter { it.matches("""\d+""".toRegex()) }.map(String::toInt)
        val operation = input[2].split("= ").last().getOperation()
        return Monkey(
            items = items.map { it.toLong() }.toMutableList(),
            operationType = operation,
            divisibleNumber = input[3].lastInt(),
            indexForTrue = input[4].lastInt(),
            indexForFalse = input[5].lastInt()
        )
    }

    private fun List<Monkey>.playTimes(times: Int, useProduct: Boolean = false) = apply {
        repeat(times) {
            play(useProduct)
        }
    }

    private fun List<Monkey>.solve() =
        map(Monkey::counter).sorted().reversed().take(2).let { it.first() * it.last() }.toString()

    private fun List<Monkey>.play(useProduct: Boolean = false) = map { monkey ->
        monkey.items.forEach { item ->
            monkey.counter += 1
            val newItem = monkey.operationType.operate(item).let { if (useProduct) it % product() else it / 3 }
            val newIndex = if (newItem % monkey.divisibleNumber == 0L) monkey.indexForTrue else monkey.indexForFalse
            this[newIndex].items.add(newItem)
        }
        monkey.clearList()
        monkey
    }

    private fun String.getOperation() = when {
        matches("""^\D+$""".toRegex()) -> MonkeyOperation.MonkeySquare()
        contains("*") -> MonkeyOperation.MonkeyMultiply(lastLong())
        contains("+") -> MonkeyOperation.MonkeyAdd(lastLong())
        else -> error("should not be reached")
    }

    private fun String.lastLong() = split(" ").last().toLong()
    private fun String.lastInt() = lastLong().toInt()

    private fun List<Monkey>.product() = map(Monkey::divisibleNumber).let {
        it.reduce { acc, number -> acc * number }
    }

    class Monkey(
        val items: MutableList<Long>,
        val operationType: MonkeyOperation,
        val divisibleNumber: Int,
        val indexForTrue: Int,
        val indexForFalse: Int,
        var counter: Long = 0
    ) {
        fun clearList() {
            items.clear()
        }
    }

    sealed class MonkeyOperation(open val constant: Long) {
        abstract fun operate(operand: Long): Long

        class MonkeyAdd(override val constant: Long) : MonkeyOperation(constant) {
            override fun operate(operand: Long) = constant + operand
        }

        class MonkeyMultiply(override val constant: Long) : MonkeyOperation(constant) {
            override fun operate(operand: Long) = constant * operand
        }

        class MonkeySquare : MonkeyOperation(0) {
            override fun operate(operand: Long) = operand * operand
        }
    }
}


