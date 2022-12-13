package day

import util.FileReader.getExampleList
import util.FileReader.getInputList

object Day01 : Day("01","24000",  "45000") {
    override fun examplePartOne() = getMaxElf(getExampleList(day))
    override fun examplePartTwo() = getMaxElves(getExampleList(day))
    override fun solvePartOne() = getMaxElf(getInputList(day))
    override fun solvePartTwo() = getMaxElves(getInputList(day))

    private fun getMaxElf(list: List<String>) = getElves(list).max().toString()

    private fun getMaxElves(list: List<String>) = getElves(list).sorted().reversed().take(3).sum().toString()

    private fun getElves(list: List<String>) = list
        .joinToString("-")
        .split("--")
        .map { it.split("-").sumOf(String::toInt) }
}