package day

import util.FileReader


object Day06 : Day("06","11", "26") {
    override fun examplePartOne() = FileReader.getExampleList("06").findMarker(4)
    override fun examplePartTwo() = FileReader.getExampleList("06").findMarker(14)
    override fun solvePartOne() = FileReader.getInputList("06").findMarker(4)
    override fun solvePartTwo() = FileReader.getInputList("06").findMarker(14)

    private fun List<String>.findMarker(length: Int): String {
        val list = this.first().toList()
        list.forEachIndexed { index, _ ->
            if (list.subList(index, index + length).toSet().size == length) return (index + length).toString()
        }
        return ""
    }
}
