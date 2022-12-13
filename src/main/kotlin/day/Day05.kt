package day

object Day05 : Day("05", "CMZ", "MCD") {
    override fun examplePartOne() = getExampleList().organizeOne()
    override fun examplePartTwo() = getExampleList().organizeTwo()
    override fun solvePartOne() = getInputList().organizeOne()
    override fun solvePartTwo() = getInputList().organizeTwo()


    private fun List<String>.organizeOne(): String {
        val (stack, operations) = this.organize()
        operations.forEach { handleOperationOne(stack, it) }
        return stack.joinToString("") { it.first() }
    }

    private fun List<String>.organizeTwo(): String {
        val (stack, operations) = this.organize()
        operations.forEach { handleOperationTwo(stack, it) }
        return stack.joinToString("") { it.first() }
    }

    private fun List<String>.organize() =
        this.getStackData().toInputList().toStack() to this.getOperationsData().getOperations()

    private fun handleOperationOne(stacks: List<ArrayDeque<String>>, operations: Triple<Int, Int, Int>) {
        val (times, from, to) = operations
        repeat(times) {
            stacks[to].addFirst(stacks[from].removeFirst())
        }
    }

    private fun handleOperationTwo(stacks: List<ArrayDeque<String>>, operations: Triple<Int, Int, Int>) {
        val (times, from, to) = operations
        stacks[from].take(times).reversed().forEach {
            stacks[to].addFirst(it)
            stacks[from].removeFirst()
        }
    }

    private fun List<String>.getStackData() = this.filter { it.contains("[") }

    private fun List<String>.getOperationsData() = this.filter { it.contains("move") }

    private fun List<String>.toInputList() =
        this.getStackData().map { row -> row.chunked(4).map { entry -> entry.replace("""\W""".toRegex(), "") } }

    private fun List<List<String>>.toStack(): MutableList<ArrayDeque<String>> {
        val stacks = MutableList(this.maxOf { it.size }) { ArrayDeque<String>() }
        this.forEach { row ->
            row.forEachIndexed { index, entry ->
                if (entry.isNotBlank()) {
                    stacks[index].addLast(entry)
                }
            }
        }
        return stacks
    }

    private fun List<String>.getOperations() = this.map { it.toOperationInput() }

    private fun String.toOperationInput() = this.split("""\D""".toRegex()).filter(String::isNotBlank).run {
        Triple(get(0).toInt(), get(1).toInt() - 1, get(2).toInt() - 1)
    }
}