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

    private fun List<String>.findMarker(length: Int): String {
        val list = this.first().toList()
        list.forEachIndexed { index, _ ->
            if (index + length <= list.size && list.subList(index, index + length).toSet().size == length) {
                return (index + length).toString()
            }
        }
        return ""
    }
}
