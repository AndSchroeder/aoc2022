package day

import kotlin.math.max
import kotlin.math.min

object Day18 : Day("18", "64", "58") {
    override fun examplePartOne() = getExampleList().solveOne()
    override fun examplePartTwo() = getExampleList().solveTwo()
    override fun solvePartOne() = getInputList().solveOne()
    override fun solvePartTwo() = getInputList().solveTwo()

    private fun List<String>.solveOne() = MagmaGrid.fillData(this).openSites.toString()
    private fun List<String>.solveTwo() = MagmaGrid.fillData(this, true).sumReachableOutside().toString()

    data class MagmaGrid(
        val magmaCoordinates: MutableMap<MagmaCoordinate, Boolean> = mutableMapOf(),
        var openSites: Int = 0,
        private var minX: Int = Int.MAX_VALUE,
        private var maxX: Int = Int.MIN_VALUE,
        private var minY: Int = Int.MAX_VALUE,
        private var maxY: Int = Int.MIN_VALUE,
        private var minZ: Int = Int.MAX_VALUE,
        private var maxZ: Int = Int.MIN_VALUE,
    ) {

        companion object {
            fun fillData(input: List<String>, withAirPockets: Boolean = false) = MagmaGrid().apply {
                input.forEach { row ->
                    val (x, y, z) = row.split(",").map(String::toInt)
                    add(x, y, z)
                }
            }
        }

        fun add(x: Int, y: Int, z: Int) {
            minX = min(x, minX)
            minY = min(y, minY)
            minZ = min(z, minZ)
            maxX = max(x, maxX)
            maxY = max(y, maxY)
            maxZ = max(z, maxZ)
            val neighborsFound = neighbors(x, y, z).count { find(it.x, it.y, it.z) == true }
            openSites += 6 - (2 * neighborsFound)
            magmaCoordinates[MagmaCoordinate(x, y, z)] = true
        }

        private fun find(x: Int, y: Int, z: Int) = magmaCoordinates[MagmaCoordinate(x, y, z)]
        private fun find(coordinate: MagmaCoordinate) = find(coordinate.x, coordinate.y, coordinate.z)

        private fun inBound(coordinate: MagmaCoordinate) = (coordinate.x in minX - 1..maxX + 1) &&
                (coordinate.y in minY - 1..maxY + 1) && (coordinate.z in minZ - 1..maxZ + 1)

        fun sumReachableOutside() = flyAroundFromOutside().let { reachableFromOutside.values.sum() }

        private fun flyAroundFromOutside(
            coordinate: MagmaCoordinate = MagmaCoordinate(minX - 1, minY - 1, minZ - 1),
            visited: MutableSet<MagmaCoordinate> = mutableSetOf()
        ) {
            if (!inBound(coordinate)) return
            visited.add(coordinate)
            val freeNeighbors = neighbors(coordinate).filter { find(it) == null }
            reachableFromOutside[coordinate] = 6 - freeNeighbors.size
            freeNeighbors.filterNot { visited.contains(it) }.forEach { flyAroundFromOutside(it, visited) }
        }

        // used because some data races I don't want to fix
        private val reachableFromOutside = mutableMapOf<MagmaCoordinate, Int>()

        private fun neighbors(coordinate: MagmaCoordinate) = neighbors(coordinate.x, coordinate.y, coordinate.z)
        private fun neighbors(x: Int, y: Int, z: Int) = listOf(
            MagmaCoordinate(x - 1, y, z),
            MagmaCoordinate(x + 1, y, z),
            MagmaCoordinate(x, y - 1, z),
            MagmaCoordinate(x, y + 1, z),
            MagmaCoordinate(x, y, z - 1),
            MagmaCoordinate(x, y, z + 1),
        )
    }

    data class MagmaCoordinate(val x: Int, val y: Int, val z: Int)
}
