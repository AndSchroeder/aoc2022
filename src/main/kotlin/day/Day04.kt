package day

import util.FileReader
import util.FileType

object Day04 : Day {
    override val examplePartOneSolution: String = "2"
    override val examplePartTwoSolution: String = "4"

    override fun examplePartOne() = FileReader.getListByFile(4, FileType.EXAMPLE).getPairs().countSubRange()
        .apply { check(this == examplePartOneSolution) }

    override fun examplePartTwo() = FileReader.getListByFile(4, FileType.EXAMPLE).getPairs().countSubContent()
        .apply { check(this == examplePartTwoSolution) }

    override fun solvePartOne() = FileReader.getListByFile(4, FileType.INPUT).getPairs().countSubRange()
    override fun solvePartTwo() = FileReader.getListByFile(4, FileType.INPUT).getPairs().countSubContent()

    private fun List<String>.getPairs() =
        this.map { team -> team.split(",").map { borders -> borders.split("-").map { border -> border.toInt() } } }

    private fun List<List<List<Int>>>.countSubRange() = this.filter { it.hasSubRange() }.size.toString()
    private fun List<List<List<Int>>>.countSubContent() = this.filter { it.hasSubContent() }.size.toString()

    private fun List<List<Int>>.hasSubRange() =
        this.first().first() <= this.last().first() && this.first().last() >= this.last().last() ||
                this.first().first() >= this.last().first() && this.first().last() <= this.last().last()

    private fun List<List<Int>>.hasSubContent() =
        this.first().last() >= this.last().first() && this.first().first() <= this.last().last()  ||
                this.first().first() >= this.last().last() && this.first().last() <= this.last().first()

}