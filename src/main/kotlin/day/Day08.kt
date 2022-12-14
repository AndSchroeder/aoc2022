package day

object Day08 : Day("08", "21", "8") {
    override fun examplePartOne() = getExampleList().getGrid().solveOne()
    override fun examplePartTwo() = getExampleList().getGrid().solveTwo()
    override fun solvePartOne() = getInputList().getGrid().solveOne()
    override fun solvePartTwo() = getInputList().getGrid().solveTwo()

    private fun List<String>.getGrid() = this.map { row -> row.toList().map { value -> value.toString().toInt() } }

    private fun List<List<Int>>.solveOne() =
        this.mapIndexed { y, list -> list.mapIndexed { x, value -> isVisible(x, y, value, this) } }
            .flatten()
            .count { it }
            .toString()

    private fun List<List<Int>>.solveTwo() =
        this.mapIndexed { y, list -> list.mapIndexed { x, value -> canSee(x, y, value, this) } }
            .flatten()
            .max()
            .toString()

    private fun isVisible(x: Int, y: Int, value: Int, grid: List<List<Int>>) =
        getBeforeX(x, grid[y]).visible(value) ||
                getAfterX(x, grid[y]).visible(value) ||
                getBeforeY(x, y, grid).visible(value) ||
                getAfterY(x, y, grid).visible(value)

    private fun canSee(x: Int, y: Int, value: Int, grid: List<List<Int>>) =
        getBeforeX(x, grid[y]).canSee(value) *
                getAfterX(x, grid[y]).canSee(value) *
                getBeforeY(x, y, grid).canSee(value) *
                getAfterY(x, y, grid).canSee(value)

    private fun getBeforeX(x: Int, list: List<Int>) = list.subList(0, x).reversed()
    private fun getAfterX(x: Int, list: List<Int>) = list.subList(x + 1, list.size)

    private fun getBeforeY(x: Int, y: Int, grid: List<List<Int>>) = grid.map { it[x] }.subList(0, y).reversed()
    private fun getAfterY(x: Int, y: Int, grid: List<List<Int>>) = grid.map { it[x] }.subList(y + 1, grid.size)

    private fun List<Int>.visible(value: Int) = all { it < value } || isEmpty()
    private fun List<Int>.canSee(value: Int) = takeWhile { it < value }.size + if (any { it >= value }) 1 else 0
}
