package day

abstract class Day(
    val day: String,
    val examplePartOneSolution: String,
    val examplePartTwoSolution: String
) {

    abstract fun examplePartOne(): String
    abstract fun examplePartTwo(): String
    abstract fun solvePartOne(): String
    abstract fun solvePartTwo(): String


}