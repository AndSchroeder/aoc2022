import day.*

fun main() {

    val days = listOf(
        Day01, Day02, Day03, Day04, Day05,
        Day06, Day07, Day08, Day09, Day10,
        Day11, Day12, Day13, Day14
    )
    days.forEach(::solveDay)
}

private fun solveDay(day: Day) {
    println("########### ${day::class.simpleName} ###########")
    println("Part 1:")
    println(
        """Example:  ${
            day.examplePartOne().apply {
                check(this == day.examplePartOneSolution) { 
                    "example part one should be ${day.examplePartOneSolution} but is $this" 
                }
            }
        }"""
    )
    println("Solution: ${day.solvePartOne()}")
    println("")
    println("Part 2:")
    println(
        """Example:  ${
            day.examplePartTwo().apply {
                check(this == day.examplePartTwoSolution) { 
                    "example part one should be ${day.examplePartTwoSolution} but is $this" 
                }
            }
        }"""
    )
    println("Solution: ${day.solvePartTwo()}")
    println()
}