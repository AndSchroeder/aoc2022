package day

import util.FileReader
import util.FileType

object Day01 : Day {
    override val examplePartOneSolution: String = "24000"
    override val examplePartTwoSolution: String = "45000"

    override fun examplePartOne() = getMaxElf(FileReader.getListByFile(1, FileType.EXAMPLE))
        .apply { check(this == examplePartOneSolution) }

    override fun examplePartTwo() = getMaxElves(FileReader.getListByFile(1, FileType.EXAMPLE))
        .apply { check(this == examplePartTwoSolution) }

    override fun solvePartOne() = getMaxElf(FileReader.getListByFile(1, FileType.INPUT))
    override fun solvePartTwo() = getMaxElves(FileReader.getListByFile(1, FileType.INPUT))

    private fun getMaxElf(list: List<String>) = getElves(list).max().toString()

    private fun getMaxElves(list: List<String>) = getElves(list).sorted().reversed().take(3).sum().toString()

    private fun getElves(list: List<String>) = list
        .joinToString("-")
        .split("--")
        .map { it.split("-").sumOf(String::toInt) }
}