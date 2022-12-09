package day

import util.FileReader
import kotlin.math.absoluteValue

object Day09 : Day {
    override val examplePartOneSolution: String = "13"
    override val examplePartTwoSolution: String = "1"

    override fun examplePartOne() = RopeGrid(FileReader.getExampleList("09")).solveOne()
        .apply { check(this == examplePartOneSolution) }

    override fun examplePartTwo() = RopeGrid(FileReader.getExampleList("09")).solveTwo()
        .apply { check(this == examplePartTwoSolution) }

    override fun solvePartOne() = RopeGrid(FileReader.getInputList("09")).solveOne()
    override fun solvePartTwo() = RopeGrid(FileReader.getInputList("09")).solveTwo()

    class RopeGrid(private val input: List<String>)  {

        private val fields = mutableListOf<RopeField>(RopeField(0,0, 1))
        private val knots = mutableListOf<RopeField>()

        fun solveOne() = solve(2)

        fun solveTwo() = solve(10)

        private fun solve(amount: Int): String {
            repeat(amount) {
                knots.add(fields.first())
            }
            input.forEach { action ->
                val operation = action.split(" ").first()
                val times = action.split(" ").last().toInt()
                operate(operation, times)
            }
            return fields.filter { it.vistedTimes > 0 }.size.toString()
        }

        private fun operate(operation: String, times: Int) {
            when (operation) {
                "R" ->  repeat(times) { goRight(knots.first()) }
                "L" ->  repeat(times) { goLeft(knots.first()) }
                "U" ->  repeat(times) { goUp(knots.first()) }
                "D" ->  repeat(times) { goDown(knots.first()) }
            }
        }

        private fun goRight(head: RopeField) = go(fields.findOrCreate(head.x + 1, head.y), 0)
        private fun goLeft(head: RopeField) = go(fields.findOrCreate(head.x - 1, head.y), 0)
        private fun goUp(head: RopeField) = go(fields.findOrCreate(head.x, head.y + 1), 0)
        private fun goDown(head: RopeField) = go(fields.findOrCreate(head.x, head.y - 1), 0)

        private fun go(newHead: RopeField, index: Int) {
            if (newHead.far(knots[index + 1])) {
                knots[index + 1] = getNewField(newHead, knots[index + 1])
                if (index == knots.maxIndex()) {
                    knots[index + 1].vistedTimes += 1
                }
            }
            knots[index] = newHead
            if (index < knots.maxIndex()) {
                go(knots[index + 1], index + 1)
            }
        }

        private fun getNewField(previous: RopeField, current: RopeField): RopeField  {
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

    class RopeField(val x: Long, val y: Long, var vistedTimes: Long = 0) {

        fun far(other: RopeField) = (this.x - other.x).absoluteValue > 1 || (this.y - other.y).absoluteValue > 1

        override fun toString(): String {
            return "x: $x, y: $y, visited: $vistedTimes"
        }
    }

    fun MutableList<RopeField>.findOrCreate(x: Long, y: Long): RopeField {
        return find { field -> field.x == x && field.y == y } ?: RopeField(x, y).also { field -> add(field) }
    }

    fun MutableList<RopeField>.maxIndex() = size - 2
}
