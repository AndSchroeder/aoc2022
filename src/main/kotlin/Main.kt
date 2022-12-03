import day.Day
import day.Day01
import day.Day02
import day.Day03

fun main(args: Array<String>) {

  val days = listOf(Day01, Day02, Day03)
  days.forEach(::solveDay)
}

private fun solveDay(day: Day) {
  println("########### ${day::class.simpleName} ###########")
  println("Part 1:")
  println("Example:  ${day.examplePartOne()}")
  println("Solution: ${day.solvePartOne()}")
  println("")
  println("Part 2:")
  println("Example:  ${day.examplePartTwo()}")
  println("Solution: ${day.solvePartTwo()}")
  println("")
}