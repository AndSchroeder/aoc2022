package day

import util.FileReader
import util.FileType

object Day02 : Day {
    override val examplePartOneSolution: String = "15"
    override val examplePartTwoSolution: String = "12"

    override fun examplePartOne() = pointsPlay(FileReader.getListByFile(2, FileType.EXAMPLE))
        .apply { check(this == examplePartOneSolution) }

    override fun examplePartTwo() = pointsOutcome(FileReader.getListByFile(2, FileType.EXAMPLE))
        .apply { check(this == examplePartTwoSolution) }

    override fun solvePartOne() = pointsPlay(FileReader.getListByFile(2, FileType.INPUT))
    override fun solvePartTwo() = pointsOutcome(FileReader.getListByFile(2, FileType.INPUT))


    private fun pointsPlay(list: List<String>) = list.map(this::matchWithPlay).sum().toString()
    private fun pointsOutcome(list: List<String>) = list.map(this::matchWithOutcome).sum().toString()

    private fun matchWithPlay(match: String) = when(match) {
        "A X" -> 4 // 1 + 3
        "A Y" -> 8 // 2 + 6
        "A Z" -> 3 // 3 + 0
        "B X" -> 1 // 1 + 0
        "B Y" -> 5 // 2 + 3
        "B Z" -> 9 // 3 + 6
        "C X" -> 7 // 1 + 6
        "C Y" -> 2 // 2 + 0
        "C Z" -> 6 // 3 + 3
        else -> 0
    }

    private fun matchWithOutcome(match: String) = when(match) {
        "A X" -> 3 // 3 + 0
        "A Y" -> 4 // 1 + 3
        "A Z" -> 8 // 2 + 6
        "B X" -> 1 // 1 + 0
        "B Y" -> 5 // 2 + 3
        "B Z" -> 9 // 3 + 6
        "C X" -> 2 // 2 + 0
        "C Y" -> 6 // 3 + 3
        "C Z" -> 7 // 1 + 6
        else -> 0
    }
}