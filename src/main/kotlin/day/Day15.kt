package day

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object Day15 : Day("15", "26", "56000011") {
    override fun examplePartOne() = getExampleList().solveOne(10)
    override fun examplePartTwo() = getExampleList().solveTwo(20, 0)
    override fun solvePartOne() = getInputList().solveOne(2_000_000)
    override fun solvePartTwo() = getInputList().solveTwo(4_000_000, 0)

    private fun List<String>.solveOne(row: Int) = create().checkRow(row).toString()

    private fun List<String>.solveTwo(max: Int, min: Int) = create(max, min).checkRows().getFrequency().toString()

    private fun List<String>.create(max: Int = Int.MAX_VALUE, min: Int = Int.MIN_VALUE) = SensorGrid().apply {
        this.max = max
        this.min = min
        forEach { row ->
            row.split(" ").map { it.replace("""[a-zA-Z]|\s|,|:|=""".toRegex(), "") }
                .filterNot(String::isBlank).map { it.toInt() }.also {
                    val (sensor, beacon) = SensorCoordinate(it[0], it[1]) to SensorCoordinate(it[2], it[3])
                    sensor.maxDistance = sensor.manhattan(beacon)
                    sensors.add(sensor)
                    beacons.add(beacon)
                    minX = max(min(minX, sensor.x - sensor.maxDistance), min)
                    maxX = min(max(maxX, sensor.x + sensor.maxDistance), max)
                }
        }
    }

    data class SensorGrid(
        val sensors: MutableList<SensorCoordinate> = mutableListOf(),
        val beacons: MutableList<SensorCoordinate> = mutableListOf(),
        var minX: Int = 0, var maxX: Int = 0, var min: Int = 0, var max: Int = 0
    ) {

        fun checkRow(y: Int) =
            (minX..maxX + 1).count() { x ->
                sensors.any { it.inSensorRange(x, y) } && beaconsInRow(y).none { beacon -> beacon.x == x }
            }

        private fun beaconsInRow(y: Int) = beacons.filter { it.y == y }

        fun checkRows() = combinations().first { (x, y) -> sensors.none { it.inSensorRange(x, y) } }

        private fun combinations() = mutableListOf<Pair<SensorCoordinate, SensorCoordinate>>().apply {
            sensors.indices.forEach { i ->
                sensors.indices.minus(0..i).forEach() { j -> this.add(sensors[i] to sensors[j]) }
            }
        }.flatMap { (s1, s2) -> (0..s1.maxDistance + 1).flatMap { ix -> borders(s1, s2, ix) } }.toSet()

        private fun borders(s1: SensorCoordinate, s2: SensorCoordinate, ix: Int) =
            mutableSetOf<Pair<Int, Int>>().apply {
                val iy = s1.maxDistance + 1 - ix
                if (s2.atBorder(s1.x + ix, s1.y + iy)) add(s1.x + ix to s1.y + iy)
                if (s2.atBorder(s1.x - ix, s1.y + iy)) add(s1.x - ix to s1.y + iy)
                if (s2.atBorder(s1.x + ix, s1.y - iy)) add(s1.x + ix to s1.y - iy)
                if (s2.atBorder(s1.x - ix, s1.y - iy)) add(s1.x - ix to s1.y - iy)
            }.filter { it.first in min..max && it.second in min..max }
    }

    data class SensorCoordinate(
        val x: Int,
        val y: Int,
        var maxDistance: Int = 0,
    ) {

        fun manhattan(other: SensorCoordinate) =
            manhattan(other.x, other.y)

        fun inSensorRange(x: Int, y: Int) = (maxDistance - abs(this.y - y))
            .let { max ->
                x >= this.x - max && x <= this.x + max
            }

        fun atBorder(x: Int, y: Int) =
            maxDistance + 1 == manhattan(x, y)

        private fun manhattan(otherX: Int, otherY: Int) =
            abs(min(x, otherX) - max(x, otherX)) + abs(min(y, otherY) - max(y, otherY))
    }

    private fun Pair<Int, Int>.getFrequency() = first.toLong() * 4_000_000L + second.toLong()
}
