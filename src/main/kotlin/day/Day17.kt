package day

object Day17 : Day("17", "3068", "1514285714288") {
    override fun examplePartOne() = getExampleList().solve(2022)
    override fun examplePartTwo() = getExampleList().solve(1_000_000_000_000)
    override fun solvePartOne() = getInputList().solve(2022)
    override fun solvePartTwo() = getInputList().solve(1_000_000_000_000)

    private fun List<String>.solve(rocks: Long) =
        Cave(width = 7, maxRocks = rocks, actions = this[0].map { Steam.from(it) }).run {
            createRocks()
            finalHeight()
        }.toString()

    data class Cave(
        var grid: MutableMap<Pair<Long, Long>, ContentType> = mutableMapOf(),
        val actions: List<Steam>,
        val width: Long,
        val maxRocks: Long,
        private var rocks: Long = 0,
        private var movesIndex: Int = 0,
        private var rocksIndex: Int = 0,
        private var states: MutableMap<StateKey, StateValue> = mutableMapOf(),
        private var cachedStoneHeight: Long = 0
    ) {

        init {
            (0 until width).forEach { x -> grid[x to 0] = ContentType.AIR }
        }

        fun createRocks() {
            while (rocks < maxRocks) {
                val circle = states[StateKey(getSnippet(), rocksIndex, movesIndex)]

                // We were here before, so we know we will only repeat a sequence of actions
                if (circle != null) {
                    val (rocksDiff, heightDiff) = (rocks - circle.rocksGained) to (heightStone() - circle.heightGained)
                    val times = (maxRocks - rocks) / (rocksDiff)
                    cachedStoneHeight += heightDiff * times
                    rocks += rocksDiff * times
                }
                createRock()
            }
        }

        private fun createRock() {

            states[StateKey(getSnippet(), rocksIndex, movesIndex)] =
                StateValue(rocks, heightStone())

            addAirLines()
            val shape = getNextShape()
            while (!shape.ready) {
                shape.move(this, getNextAction())
            }
        }

        private fun addAirLines() {

            (heightStone() until heightStone() + 3).forEach { y ->
                (0 until width).forEach { x ->
                    grid[x to y] = ContentType.AIR
                }
            }
        }

        private fun getNextAction() = actions[movesIndex % actions.size]
            .apply { movesIndex = (movesIndex + 1) % actions.size }

        private fun getNextShape() = when (rocksIndex) {
            0 -> Shape.HorizontalLine(2, height() + 1)
            1 -> Shape.Plus(2, height() + 1)
            2 -> Shape.Ankle(2, height() + 1)
            3 -> Shape.VerticalLine(2, height() + 1)
            4 -> Shape.Quadratic(2, height() + 1)
            else -> error("unreachable")
        }.apply {
            rocksIndex = (rocksIndex + 1) % 5
            rocks += 1
        }

        fun finalHeight() = cachedStoneHeight + heightStone()

        private fun heightStone() =
            (grid.filter { it.value == ContentType.STONE }.keys.maxOfOrNull { (_, y) -> y }
                ?: -1) + 1

        private fun height() = grid.keys.maxOfOrNull { (_, y) -> y } ?: 0

        private fun getSnippet() = getHeights().let {
            val (min, max) = it.min() to it.max()
            mutableMapOf<Pair<Long, Long>, ContentType>().also { newGrid ->
                (min..max).forEach { y ->
                    (0 until width).forEach { x ->
                        newGrid[x to (y - min)] = grid[x to y] ?: ContentType.AIR
                    }
                }
            }
        }

        private fun getHeights() = (0 until width).map { x ->
            grid.filter { position -> position.key.first == x && position.value == ContentType.STONE }
                .map { position -> position.key.second }.maxOrNull() ?: 0
        }

        override fun toString() = (0..height()).reversed().joinToString("\n") { y ->
            "|" + (0 until width).joinToString("") { x ->
                grid[x to y]?.symbol ?: "."
            } + "|"
        } + "\n+" + ((0 until width)).joinToString("") { "—" } + "+"
    }

    data class StateKey(val state: MutableMap<Pair<Long, Long>, ContentType>, val rocksIndex: Int, val movesIndex: Int)
    data class StateValue(val rocksGained: Long, val heightGained: Long)

    sealed class Shape(
        open var x: Long,
        open var y: Long,
        private val content: List<List<ContentType>>,
        var ready: Boolean = false
    ) {
        fun move(cave: Cave, nextAction: Steam) {
            when (nextAction) {
                Steam.LEFT -> moveLeft(cave)
                Steam.RIGHT -> moveRight(cave)
            }
            moveDown(cave)
            if (ready) placeShape(cave)
        }

        private fun moveLeft(cave: Cave) {
            if (x == 0L) return
            content.forEachIndexed { iy, row ->
                row.forEachIndexed { ix, type ->
                    if (type == ContentType.STONE && cave.grid[x + ix - 1 to y + iy] == ContentType.STONE) return
                }
            }
            x -= 1
        }

        private fun moveRight(cave: Cave) {
            if (x + content[0].size == cave.width) return
            content.forEachIndexed { iy, row ->
                row.forEachIndexed { ix, type ->
                    if (type == ContentType.STONE && cave.grid[x + ix + 1 to y + iy] == ContentType.STONE) return
                }
            }
            x += 1
        }

        private fun moveDown(cave: Cave) {
            if (y == 0L) {
                ready = true
                return
            }
            content.forEachIndexed { iy, row ->
                row.forEachIndexed { ix, type ->
                    if (type == ContentType.STONE && cave.grid[x + ix to y + iy - 1] == ContentType.STONE) {
                        ready = true
                        return
                    }
                }
            }
            y -= 1
        }

        private fun placeShape(cave: Cave) {
            content.forEachIndexed { iy, row ->
                row.forEachIndexed { ix, content ->
                    if (content == ContentType.STONE) cave.grid[x + ix to y + iy] = content
                }
            }
        }

        class HorizontalLine(override var x: Long, override var y: Long) : Shape(
            x, y, listOf(
                listOf(ContentType.STONE, ContentType.STONE, ContentType.STONE, ContentType.STONE)
            )
        )

        class Plus(override var x: Long, override var y: Long) : Shape(
            x, y, listOf(
                listOf(ContentType.AIR, ContentType.STONE, ContentType.AIR),
                listOf(ContentType.STONE, ContentType.STONE, ContentType.STONE),
                listOf(ContentType.AIR, ContentType.STONE, ContentType.AIR),
            )
        )

        class Ankle(override var x: Long, override var y: Long) : Shape(
            x, y, listOf(
                listOf(ContentType.AIR, ContentType.AIR, ContentType.STONE),
                listOf(ContentType.AIR, ContentType.AIR, ContentType.STONE),
                listOf(ContentType.STONE, ContentType.STONE, ContentType.STONE),
            ).reversed()
        )

        class VerticalLine(override var x: Long, override var y: Long) : Shape(
            x, y, listOf(
                listOf(ContentType.STONE),
                listOf(ContentType.STONE),
                listOf(ContentType.STONE),
                listOf(ContentType.STONE),
            )
        )

        class Quadratic(override var x: Long, override var y: Long) : Shape(
            x, y, listOf(
                listOf(ContentType.STONE, ContentType.STONE),
                listOf(ContentType.STONE, ContentType.STONE),
            )
        )
    }

    enum class ContentType(val symbol: String) {
        AIR("."),
        STONE("#")
    }

    enum class Steam {
        LEFT,
        RIGHT;

        companion object {
            fun from(symbol: Char) = when (symbol) {
                '<' -> LEFT
                '>' -> RIGHT
                else -> error("Not parsable")
            }
        }
    }
}
