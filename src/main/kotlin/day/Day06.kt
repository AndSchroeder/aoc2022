package day

import util.FileReader


object Day06 : Day {
    override val examplePartOneSolution: String = "11"
    override val examplePartTwoSolution: String = "26"

    override fun examplePartOne() = FileReader.getExampleList(6).findMarker(4)
        .apply { check(this == examplePartOneSolution) }

    override fun examplePartTwo() = FileReader.getExampleList(6).findMarker(14)
        .apply { check(this == examplePartTwoSolution) }

    override fun solvePartOne() = FileReader.getInputList(6).findMarker(4)
    override fun solvePartTwo() = FileReader.getInputList(6).findMarker(14)

    private fun List<String>.findMarker(length: Int) = this.first().toSequences(length).ofLength(length)

    private fun MutableList<Set<Char>>.ofLength(length: Int): String {
        this.forEachIndexed { index, set ->
            if (set.size == length) {
                return (index + length).toString()
            }
        }
        return ""
    }

    private fun String.toSequences(length: Int) =
        mutableListOf<Set<Char>>().also { list ->
            this.toList().forEachIndexed { index, _ ->
                if (index + length <= this.length)
                    list.add(this.toList().subList(index, index + length).toSet())
            }
        }
}