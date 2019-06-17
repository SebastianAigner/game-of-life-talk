package sample

enum class CellState {
    DEAD,
    ALIVE
}

val emptyWorld = World(Array(10) {
    Array(10) {
        CellState.DEAD
    }
})

val gliderWorld = emptyWorld
    .set(4, 3, CellState.ALIVE)
    .set(5, 4, CellState.ALIVE)
    .set(5, 5, CellState.ALIVE)
    .set(3, 5, CellState.ALIVE)
    .set(4, 5, CellState.ALIVE)

fun main() {
    var world = gliderWorld
    repeat(5) {
        println(world.toText())
        world = world.map(World::conwayRules)
    }
}

class World(private val matrix: Array<Array<CellState>>) {
    val width = 10
    val height = 10

    fun get(x: Int, y: Int) = matrix[y][x]
    fun set(x: Int, y: Int, cellState: CellState): World {
        val newMatrix = matrix.map { it.copyOf() }.toTypedArray()
        newMatrix[y][x] = cellState
        return World(newMatrix)
    }

    fun countNeighbours(x: Int, y: Int): Int {
        var aliveNeighbors = 0
        for (column in (x - 1)..(x + 1)) {
            for (row in (y - 1)..(y + 1)) {
                if (column == x && row == y) continue
                if (column !in 0 until width) continue
                if (row !in 0 until height) continue
                if (get(column, row) == CellState.ALIVE) {
                    aliveNeighbors++
                }
            }
        }
        return aliveNeighbors
    }

    fun map(ruleset: World.(x: Int, y: Int, CellState) -> CellState): World {
        var newWorld = this
        forEach { x, y, cellState ->
            newWorld = newWorld.set(
                x,
                y,
                ruleset(x, y, cellState)
            )
        }
        return newWorld
    }

    fun forEach(f: (x: Int, y: Int, c: CellState) -> Unit) {
        matrix.forEachIndexed { y, line ->
            line.forEachIndexed { x, cellState ->
                f(x, y, cellState)
            }
        }
    }

    fun forEachAlive(f: (x: Int, y: Int) -> Unit) {
        forEach { x, y, c ->
            if (c == CellState.ALIVE) f(x, y)
        }
    }

    fun conwayRules(x: Int, y: Int, cellState: CellState): CellState {
        return when (cellState) {
            CellState.ALIVE ->
                when (countNeighbours(x, y)) {
                    in 2..3 -> CellState.ALIVE
                    else -> CellState.DEAD
                }
            CellState.DEAD -> {
                when (countNeighbours(x, y)) {
                    3 -> CellState.ALIVE
                    else -> CellState.DEAD
                }
            }
        }
    }
}

fun World.toText(): String {
    val sb = StringBuilder()
    for (y in 0 until height) {
        for (x in 0 until width) {
            sb.append(
                when (get(x, y)) {
                    CellState.ALIVE -> "🐛"
                    CellState.DEAD -> "🕸"
                }
            )
        }
        sb.append("\n")
    }
    return sb.toString()
}