package day

import util.FileReader

object Day12 : Day("12", "31", "29") {
    override fun examplePartOne() = FileReader.getExampleList(day).solve()
    override fun examplePartTwo() = FileReader.getExampleList(day).solve(true)
    override fun solvePartOne() = FileReader.getInputList(day).solve()
    override fun solvePartTwo() = FileReader.getInputList(day).solve(true)

    private fun List<String>.solve(partTwo: Boolean = false): String {
        val grid = HeightGrid()
        grid.fillList(this, partTwo)
        grid.dijkstra(grid.start)
        return grid.end.costs.toString()
    }

    data class HeightGrid(
        val coordinates: MutableList<HeightCoordinate> = mutableListOf(),
        var start: HeightCoordinate = HeightCoordinate(0, 0, 0),
        var end: HeightCoordinate = HeightCoordinate(0, 0, 0)
    ) {

        fun fillList(list: List<String>, partTwo: Boolean = false) {
            list.forEachIndexed { indexY, row ->
                row.forEachIndexed { indexX, heightChar ->
                    val coordinate = HeightCoordinate(indexX, indexY, heightChar.height())
                    coordinates.add(coordinate)
                    if (heightChar == 'S') {
                        start = coordinate
                    }
                    if (heightChar == 'E') end = coordinate
                    if ((partTwo && heightChar.height() == 0) || heightChar == 'S') coordinate.costs = 0
                }
            }
        }

        fun dijkstra(coordinate: HeightCoordinate) {
            coordinate.visited = true
            val costs = coordinate.costs + 1
            neighbors(coordinate).filter { it.costs > costs }.forEach { it.costs = costs }
            firstUnvisited()?.let { dijkstra(it) }
        }

        private fun find(x: Int, y: Int, z: Int) = coordinates.find { it.x == x && it.y == y && it.z <= z + 1 }

        private fun neighbors(coordinate: HeightCoordinate) = listOfNotNull(
            find(coordinate.x - 1, coordinate.y, coordinate.z),
            find(coordinate.x + 1, coordinate.y, coordinate.z),
            find(coordinate.x, coordinate.y - 1, coordinate.z),
            find(coordinate.x, coordinate.y + 1, coordinate.z),
        )

        private fun firstUnvisited() =
            coordinates.filterNot(HeightCoordinate::visited).sortedBy(HeightCoordinate::costs).firstOrNull()
    }

    data class HeightCoordinate(
        val x: Int,
        val y: Int,
        val z: Int,
        var costs: Int = Int.MAX_VALUE,
        var visited: Boolean = false
    )

    private fun Char.height() = when (this) {
        'S' -> 0
        'E' -> 25
        else -> this - 'a'
    }
}
