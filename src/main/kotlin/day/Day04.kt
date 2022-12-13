package day

import util.FileReader.getExampleList
import util.FileReader.getInputList

object Day04 : Day("04", "2", "4") {
    override fun examplePartOne() = getExampleList(day).getPairs().countSubRange()
    override fun examplePartTwo() = getExampleList(day).getPairs().countSubContent()
    override fun solvePartOne() = getInputList(day).getPairs().countSubRange()
    override fun solvePartTwo() = getInputList(day).getPairs().countSubContent()

    private fun List<String>.getPairs() =
        this.map { team -> team.split(",").map { borders -> borders.split("-").map { border -> border.toInt() } } }

    private fun List<List<List<Int>>>.countSubRange() = this.filter { it.hasSubRange() }.size.toString()
    private fun List<List<List<Int>>>.countSubContent() = this.filter { it.hasSubContent() }.size.toString()

    private fun List<List<Int>>.hasSubRange() =
        this.first().first() <= this.last().first() && this.first().last() >= this.last().last() ||
                this.first().first() >= this.last().first() && this.first().last() <= this.last().last()

    private fun List<List<Int>>.hasSubContent() =
        this.first().last() >= this.last().first() && this.first().first() <= this.last().last() ||
                this.first().first() >= this.last().last() && this.first().last() <= this.last().first()

}