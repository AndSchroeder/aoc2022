package day

import util.FileReader

object Day10 : Day {
    override val examplePartOneSolution: String = "13140"
    override val examplePartTwoSolution: String = "##..##..##..##..##..##..##..##..##..##..\n" +
            "###...###...###...###...###...###...###.\n" +
            "####....####....####....####....####....\n" +
            "#####.....#####.....#####.....#####.....\n" +
            "######......######......######......####\n" +
            "#######.......#######.......#######....."

    override fun examplePartOne() = FileReader.getExampleList("10").solveOne()
        .apply { check(this == examplePartOneSolution) }

    override fun examplePartTwo() = FileReader.getExampleList("10").solveTwo().toString()
        .apply { check(this == examplePartTwoSolution) }

    override fun solvePartOne() = FileReader.getInputList("10").solveOne()
    override fun solvePartTwo() = FileReader.getInputList("10").solveTwo()


    private fun List<String>.solveOne() = ElfMachine().operate(this).solveOne()

    private fun List<String>.solveTwo() = ElfMachine().operate(this).solveTwo()

    class ElfMachine(private val states: MutableList<ElfState> = mutableListOf()) {

        fun operate(commands: List<String>) = commands.forEach(::operate).let { this }

        fun solveOne() = listOf(20, 60, 100, 140, 180, 220).sumOf(::getSignalStrength).toString()

        fun solveTwo() = states.chunked(40).joinToString("\n") { chunk ->
            chunk.mapIndexed { index, elfState ->
                if (listOf(elfState.during - 1, elfState.during, elfState.during + 1).contains(index)) "#" else "."
            }.joinToString("")
        }

        private fun operate(command: String) {
            when {
                command.startsWith("noop") -> noop()
                command.startsWith("addx") -> addX(command.split(" ").last().toInt())
            }
        }

        private fun addX(add: Int) {
            noop()
            states.add(ElfState.after(lastState(), add))
        }

        private fun noop() {
            states.add(ElfState.after(lastState()))
        }

        private fun lastState() = states.lastOrNull()
        private fun getSignalStrength(at: Int) = at * states[at - 1].during
    }

    class ElfState(val during: Int, val after: Int) {
        companion object {

            fun after(other: ElfState?, add: Int = 0) =
                other?.let { ElfState(other.after, other.after + add) } ?: ElfState(1, 1 + add)
        }
    }
}
