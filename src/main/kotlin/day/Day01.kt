package day

object Day01 : Day("01", "24000", "45000") {
    override fun examplePartOne() = getMaxElf(getExampleList())
    override fun examplePartTwo() = getMaxElves(getExampleList())
    override fun solvePartOne() = getMaxElf(getInputList())
    override fun solvePartTwo() = getMaxElves(getInputList())

    private fun getMaxElf(list: List<String>) = getElves(list).max().toString()

    private fun getMaxElves(list: List<String>) = getElves(list).sorted().reversed().take(3).sum().toString()

    private fun getElves(list: List<String>) = list
        .joinToString("-")
        .split("--")
        .map { it.split("-").sumOf(String::toInt) }
}