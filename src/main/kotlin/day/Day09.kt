package day

import kotlin.math.absoluteValue

object Day09 : Day("09", "13", "1") {
    override fun examplePartOne() = RopeGrid(getExampleList()).solveOne()
    override fun examplePartTwo() = RopeGrid(getExampleList()).solveTwo()
    override fun solvePartOne() = RopeGrid(getInputList()).solveOne()
    override fun solvePartTwo() = RopeGrid(getInputList()).solveTwo()

    class RopeGrid(private val input: List<String>) {

        private val fields = mutableListOf<RopeField>(RopeField(0, 0, 1))
        private val knots = mutableListOf<RopeField>()

        fun solveOne() = solve(2)

        fun solveTwo() = solve(10)

        private fun solve(amount: Int): String {
            repeat(amount) { knots.add(fields.first()) }
            input.forEach { action ->
                val (operation, times) = action.split(" ")
                operate(operation, times)
            }
            return fields.filter { it.visitedTimes > 0 }.size.toString()
        }

        private fun operate(operation: String, times: String) {
            repeat(times.toInt()) {
                when (operation) {
                    "R" -> goRight(knots.first())
                    "L" -> goLeft(knots.first())
                    "U" -> goUp(knots.first())
                    "D" -> goDown(knots.first())
                }
            }
        }

        private fun goRight(head: RopeField) = go(fields.findOrCreate(head.x + 1, head.y), 0)
        private fun goLeft(head: RopeField) = go(fields.findOrCreate(head.x - 1, head.y), 0)
        private fun goUp(head: RopeField) = go(fields.findOrCreate(head.x, head.y + 1), 0)
        private fun goDown(head: RopeField) = go(fields.findOrCreate(head.x, head.y - 1), 0)

        private fun go(newHead: RopeField, index: Int) {
            if (newHead.far(knots[index + 1])) {
                knots[index + 1] = getNewField(newHead, knots[index + 1])
                if (index == knots.maxIndex()) knots[index + 1].visitedTimes += 1
            }
            knots[index] = newHead
            if (index < knots.maxIndex()) go(knots[index + 1], index + 1)
        }

        private fun getNewField(previous: RopeField, current: RopeField): RopeField {
            val xDiffer = previous.x - current.x
            val yDiffer = previous.y - current.y
            return when {
                xDiffer == 0L && yDiffer > 0 -> fields.findOrCreate(current.x, current.y + 1)
                xDiffer == 0L && yDiffer < 0 -> fields.findOrCreate(current.x, current.y - 1)
                xDiffer > 0 && yDiffer == 0L -> fields.findOrCreate(current.x + 1, current.y)
                xDiffer > 0 && yDiffer > 0 -> fields.findOrCreate(current.x + 1, current.y + 1)
                xDiffer > 0 -> fields.findOrCreate(current.x + 1, current.y - 1)
                xDiffer < 0 && yDiffer == 0L -> fields.findOrCreate(current.x - 1, current.y)
                xDiffer < 0 && yDiffer > 0 -> fields.findOrCreate(current.x - 1, current.y + 1)
                xDiffer < 0 -> fields.findOrCreate(current.x - 1, current.y - 1)
                else -> error("unreachable")
            }
        }
    }

    class RopeField(val x: Long, val y: Long, var visitedTimes: Long = 0) {

        fun far(other: RopeField) = (this.x - other.x).absoluteValue > 1 || (this.y - other.y).absoluteValue > 1
    }

    fun MutableList<RopeField>.findOrCreate(x: Long, y: Long): RopeField {
        return find { field -> field.x == x && field.y == y } ?: RopeField(x, y).also { field -> add(field) }
    }

    fun MutableList<RopeField>.maxIndex() = size - 2
}
