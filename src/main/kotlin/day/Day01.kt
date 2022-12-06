package day

import util.FileReader.getExampleList
import util.FileReader.getInputList

object Day01 : Day {
    override val examplePartOneSolution: String = "24000"
    override val examplePartTwoSolution: String = "45000"

    override fun examplePartOne() = getMaxElf(getExampleList("01"))
        .apply { check(this == examplePartOneSolution) }

    override fun examplePartTwo() = getMaxElves(getExampleList("01"))
        .apply { check(this == examplePartTwoSolution) }

    override fun solvePartOne() = getMaxElf(getInputList("01"))
    override fun solvePartTwo() = getMaxElves(getInputList("01"))

    private fun getMaxElf(list: List<String>) = getElves(list).max().toString()

    private fun getMaxElves(list: List<String>) = getElves(list).sorted().reversed().take(3).sum().toString()

    private fun getElves(list: List<String>) = list
        .joinToString("-")
        .split("--")
        .map { it.split("-").sumOf(String::toInt) }
}