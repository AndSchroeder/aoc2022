package day

import util.FileReader

object Day12 : Day {
    override val examplePartOneSolution: String = "31"
    override val examplePartTwoSolution: String = "29"

    override fun examplePartOne() = FileReader.getExampleList("12").solve()
        .apply { check(this == examplePartOneSolution) }

    override fun examplePartTwo() = FileReader.getExampleList("12").solve(true)
        .apply { check(this == examplePartTwoSolution) }

    override fun solvePartOne() = FileReader.getInputList("12").solve()
    override fun solvePartTwo() = FileReader.getInputList("12").solve(true)

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
                    val height = when (heightChar) {
                        'S' -> 0
                        'E' -> 25
                        else -> heightChar - 'a'
                    }
                    val coordinate = HeightCoordinate(indexX, indexY, height)
                    coordinates.add(coordinate)
                    if (heightChar == 'S') {
                        start = coordinate
                        start.costs = 0
                    }
                    if (heightChar == 'E') end = coordinate
                    if ((partTwo && height == 0)) coordinate.costs = 0
                }
            }
        }

        fun dijkstra(coordinate: HeightCoordinate) {
            coordinate.visited = true
            val costs = coordinate.costs + 1
            neighbors(coordinate).filter { it.costs > costs }.forEach { it.costs = costs }
            firstUnvisted()?.let { dijkstra(it) }
        }

        private fun find(x: Int, y: Int, z: Int) = coordinates.find { it.x == x && it.y == y && it.z <= z + 1 }

        private fun neighbors(coordinate: HeightCoordinate) = listOfNotNull(
            find(coordinate.x - 1, coordinate.y, coordinate.z),
            find(coordinate.x + 1, coordinate.y, coordinate.z),
            find(coordinate.x, coordinate.y - 1, coordinate.z),
            find(coordinate.x, coordinate.y + 1, coordinate.z),
        )

        private fun firstUnvisted() =
            coordinates.filterNot(HeightCoordinate::visited).sortedBy(HeightCoordinate::costs).firstOrNull()
    }

    data class HeightCoordinate(
        val x: Int,
        val y: Int,
        val z: Int,
        var costs: Int = Int.MAX_VALUE,
        var visited: Boolean = false
    )
}


