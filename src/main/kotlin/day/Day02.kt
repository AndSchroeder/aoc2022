package day

import util.FileReader.getExampleList
import util.FileReader.getInputList

object Day02 : Day("02", "15", "12") {
    override fun examplePartOne() = pointsPlay(getExampleList(day))
    override fun examplePartTwo() = pointsOutcome(getExampleList(day))
    override fun solvePartOne() = pointsPlay(getInputList(day))
    override fun solvePartTwo() = pointsOutcome(getInputList(day))

    private fun pointsPlay(list: List<String>) = list.map(this::matchWithPlay).sum().toString()
    private fun pointsOutcome(list: List<String>) = list.map(this::matchWithOutcome).sum().toString()

    private fun matchWithPlay(match: String) = when (match) {
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

    private fun matchWithOutcome(match: String) = when (match) {
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