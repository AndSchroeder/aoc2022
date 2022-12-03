package day

import util.FileReader
import util.FileType

object Day03 : Day {
    override val examplePartOneSolution: String = "157"
    override val examplePartTwoSolution: String = "70"

    override fun examplePartOne() = FileReader.getListByFile(3, FileType.EXAMPLE).getCharScoresOne()
        .apply { check(this == examplePartOneSolution) }

    override fun examplePartTwo() = FileReader.getListByFile(3, FileType.EXAMPLE).getCharScoresTwo()
        .apply { check(this == examplePartTwoSolution) }

    override fun solvePartOne() = FileReader.getListByFile(3, FileType.INPUT).getCharScoresOne()

    override fun solvePartTwo() = FileReader.getListByFile(3, FileType.INPUT).getCharScoresTwo()

    fun List<String>.getCharScoresOne() = this.map { it.splitAtHalf().inBoth().score() }.sum().toString()

    fun List<String>.getCharScoresTwo() = this.splitInGroups().map { it.inAll().score() }.sum().toString()

    private fun String.splitAtHalf() =
        this.substring(0..this.length / 2).toSet() to this.substring(this.length / 2).toSet()

    private fun List<String>.splitInGroups() =
        this.chunked(3).map { Triple(it[0].toCharSet(), it[1].toCharSet(), it[2].toCharSet()) }

    private fun Pair<Set<Char>, Set<Char>>.cartesian() =
        this.first.flatMap { one -> this.second.map { two -> one to two } }

    private fun Triple<Set<Char>, Set<Char>, Set<Char>>.cartesian() =
        this.first.flatMap { one -> this.second.flatMap { two -> this.third.map { three -> Triple(one, two, three) } } }

    private fun Pair<Set<Char>, Set<Char>>.inBoth() =
        cartesian().first { it.first == it.second }.first

    private fun Triple<Set<Char>, Set<Char>, Set<Char>>.inAll() =
        cartesian().first { it.first == it.second && it.first == it.third }.first

    private fun Char.score() = if (this.isLowerCase()) (this.minus('a') + 1) else (this.minus('A') + 27)

    private fun String.toCharSet() = this.toCharArray().toSet()
}