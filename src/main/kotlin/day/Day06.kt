package day

object Day06 : Day("06", "11", "26") {
    override fun examplePartOne() = getExampleList().findMarker(4)
    override fun examplePartTwo() = getExampleList().findMarker(14)
    override fun solvePartOne() = getInputList().findMarker(4)
    override fun solvePartTwo() = getInputList().findMarker(14)

    private fun List<String>.findMarker(length: Int): String {
        val list = this.first().toList()
        list.forEachIndexed { index, _ ->
            if (list.subList(index, index + length).toSet().size == length) return (index + length).toString()
        }
        return ""
    }
}
