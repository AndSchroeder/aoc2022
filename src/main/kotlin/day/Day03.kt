package day

import util.FileReader.getExampleList
import util.FileReader.getInputList

object Day03 : Day {
    override val examplePartOneSolution: String = "157"
    override val examplePartTwoSolution: String = "70"

    override fun examplePartOne() = getExampleList("03").getCharScoresOne()
        .apply { check(this == examplePartOneSolution) }

    override fun examplePartTwo() = getExampleList("03").getCharScoresTwo()
        .apply { check(this == examplePartTwoSolution) }

    override fun solvePartOne() = getInputList("03").getCharScoresOne()
    override fun solvePartTwo() = getInputList("03").getCharScoresTwo()

    private fun List<String>.getCharScoresOne() = this.sumOf { it.splitAtHalf().inBoth().score() }.toString()

    private fun List<String>.getCharScoresTwo() = this.splitInGroups().sumOf { it.inAll().score() }.toString()

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