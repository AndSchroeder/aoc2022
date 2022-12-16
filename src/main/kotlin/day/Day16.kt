package day

object Day16 : Day("16", "1651", "1707") {
    override fun examplePartOne() = getExampleList().solve()
    override fun examplePartTwo() = getExampleList().solve(true)
    override fun solvePartOne() = getInputList().solve()
    override fun solvePartTwo() = getInputList().solve(true)

    private fun List<String>.solve(useElephant: Boolean = false) = ValveLabyrinth()
        .fillData(this).getPaths().findPath(elephant = if (useElephant) FIRST to 0 else null).toString()

    const val MAX_TIME = 30
    const val MAX_TIME_ELEPHANT = 26
    const val FIRST = "AA"

    data class ValveLabyrinth(
        val valves: MutableMap<String, Valve> = mutableMapOf(),
        val ways: MutableMap<Pair<String, String>, Int> = mutableMapOf()
    ) {

        fun fillData(input: List<String>) = input.forEach() { row ->
                row.split("""( .*e=)|(;.*es? )|(Valve )""".toRegex()).filterNot(String::isBlank)
                    .also { valveInput ->
                        populate(valveInput[0], valveInput[1].toInt(), valveInput[2].split(", ").toSet())
                    }
            }.let { this }

        fun getPaths() =
            valves.values.filter { it.rate > 0 || it.id == FIRST }.forEach { valve1 ->
                valves.values.forEach { it.clear() }
                dijkstra(valve1.apply { costs = 0 })
                valves.values.filter { it.rate > 0 }.forEach { valve2 ->
                    if (valve1 != valve2) ways[valve1.id to valve2.id] = valve2.costs
                }
            }.let { this }

        fun findPath(
            me: Pair<String, Int> = FIRST to 0,
            elephant: Pair<String, Int>? = null,
            opened: Set<String> = setOf()
        ): Int = kotlin.run {
            val (location, currentTime) = me
            filteredWays(location, opened).maxOfOrNull { (path, costs) ->
                val timePassed = currentTime + costs + 1
                if (timePassed.openTime(elephant != null) <= 0) { 0 } else {
                    timePassed.openTime(elephant != null) * path.second.valve().rate +
                            if (elephant == null) {
                                findPath(path.second to timePassed, null, opened + path.second)
                            } else {
                                findPathElephant(elephant, opened, path.second, timePassed)
                            }
                }

            }
        } ?: 0

        private fun findPathElephant(
            elephant: Pair<String, Int>,
            opened: Set<String> = setOf(),
            currentValveMe: String,
            timePassedMe: Int
        ) = kotlin.run {
            val (locationElephant, currentTimeElephant) = elephant
            filteredWays(
                locationElephant,
                opened + currentValveMe
            ).maxOfOrNull { (pathElephant, costsElephant) ->
                if (pathElephant.second == currentValveMe) { -1 } else {
                    val timePassedElephant = currentTimeElephant + costsElephant + 1
                    if (timePassedElephant.openTime(true) <= 0) { 0 } else {
                        timePassedElephant.openTime(true) * pathElephant.second.valve().rate +
                                findPath(
                                    currentValveMe to timePassedMe,
                                    pathElephant.second to timePassedElephant,
                                    opened + pathElephant.second + currentValveMe
                                )
                    }
                }
            } ?: 0
        }

        private fun populate(id: String, rate: Int = 0, neighborIds: Set<String> = mutableSetOf()): Valve =
            findOrCreate(id, rate, neighborIds)
                .apply {
                    this.rate = rate
                }

        private fun findOrCreate(id: String, rate: Int = 0, neighborIds: Set<String> = mutableSetOf()): Valve =
            valves[id] ?: Valve(id, rate, neighborIds.toMutableSet()).apply { valves[id] = this }

        private fun String.valve(): Valve = valves[this]!!

        private fun filteredWays(location: String = FIRST, opened: Set<String> = setOf()) =
            ways.filter { (path, _) ->
                path.second.valve().rate > 0 && !opened.contains(path.second) && path.first == location
            }

        private fun Int.openTime(elephant: Boolean = false) = (if (elephant) MAX_TIME_ELEPHANT else MAX_TIME) - this

        private fun dijkstra(from: Valve) {
            from.visited = true
            val costs = from.costs + 1
            from.neighbors.map { it.valve() }.filter { it.costs > costs }.forEach { it.costs = costs }
            firstUnvisited()?.let { dijkstra(it) }
        }

        private fun firstUnvisited() =
            valves.values.filterNot(Valve::visited).sortedBy(Valve::costs).firstOrNull()
    }

    data class Valve(
        val id: String, var rate: Int = 0, var neighbors: MutableSet<String> = mutableSetOf(),
        var opend: Boolean = false, var visited: Boolean = false, var costs: Int = Int.MAX_VALUE
    ) {
        fun clear() {
            costs = Int.MAX_VALUE
            visited = false
        }
    }
}
