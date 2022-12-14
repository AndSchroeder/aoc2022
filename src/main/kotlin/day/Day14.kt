package day

import java.lang.Integer.max
import java.lang.Integer.min

object Day14 : Day("14", "24", "93") {
    override fun examplePartOne() = getExampleList().solve()
    override fun examplePartTwo() = getExampleList().solve(false)
    override fun solvePartOne() = getInputList().solve()
    override fun solvePartTwo() = getInputList().solve(false)

    fun List<String>.solve(withAbyss: Boolean = true): String {
        val grid = parse(withAbyss)
        return grid.fields.count { it.value.content == CaveContent.SAND }.toString()
    }

    private fun List<String>.parse(withAbyss: Boolean) = CaveGrid().also { grid ->
        parseRocks(grid, withAbyss)
        while (!grid.ended) {
            grid.trickle(withAbyss = withAbyss)
        }
    }

    private fun List<String>.parseRocks(grid: CaveGrid, withAbyss: Boolean) = forEach { row ->
        row.split(" -> ").map { it.split(",").map(String::toInt) }.zipWithNext() { first, second ->
            grid.addRockLine(second.first(), second.last(), first.first(), first.last())
        }
    }.apply {
        if (!withAbyss) {
            val (xMin, xMax) = grid.fields.minBy { it.value.x }.value.x to grid.fields.maxBy { it.value.x }.value.x
            grid.addRockLine(xMin - grid.lowest * 2, grid.lowest + 2, xMax + grid.lowest * 2, grid.lowest + 2)
        }
    }

    data class CaveGrid(
        val fields: MutableMap<Pair<Int, Int>,CaveField> = mutableMapOf(),
        var lowest: Int = 0,
        var ended: Boolean = false
    ) {

        init {
            fields[500 to 0] = CaveField(500, 0, CaveContent.START)
        }

        fun addRockLine(xStart: Int, yStart: Int, xEnd: Int, yEnd: Int) = when (xStart) {
            xEnd -> (min(yStart, yEnd) until max(yStart, yEnd) + 1).forEach { y -> addRock(xStart, y) }
            else -> (min(xStart, xEnd) until max(xStart, xEnd) + 1).forEach { x -> addRock(x, yStart) }
        }

        fun trickle(x: Int = start()?.x ?: 500, y: Int = start()?.y ?: 0, withAbyss: Boolean = true) {
            if (y > lowest && withAbyss || start() == null) {
                ended = true
                return
            }
            val firstNeighbor = neighbors(x, y).firstOrNull()
            if (firstNeighbor == null) {
                setSand(x, y)
                return
            }
            trickle(firstNeighbor.x, firstNeighbor.y, withAbyss)
        }

        private fun setSand(x: Int, y: Int) {
            findOrCreate(x, y).content = CaveContent.SAND
        }

        private fun neighbors(x: Int, y: Int) = listOf(
            findOrCreate(x, y + 1), findOrCreate(x - 1, y + 1), findOrCreate(x + 1, y + 1),
        ).filter { it.content == CaveContent.AIR || it.content == CaveContent.START }

        private fun findOrCreate(x: Int, y: Int) =
            fields[x to y] ?: CaveField(x, y, CaveContent.AIR).apply { fields[x to y] = this }

        private fun start() = fields[500 to 0]?.let { if (it.content == CaveContent.START) it else null }

        private fun addRock(x: Int, y: Int)  {
            fields[x to y] = CaveField(x, y, CaveContent.ROCK).apply { lowest = max(lowest, y) }
        }

    }

    data class CaveField(val x: Int, val y: Int, var content: CaveContent)

    enum class CaveContent {
        AIR,
        ROCK,
        SAND,
        START,
    }
}
