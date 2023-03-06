package board

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)


open class SquareBoardImpl(override val width: Int) : SquareBoard {

    private val cellMatrix: List<List<Cell>>

    init {
        // i = row
        // j = column
        cellMatrix = (1..width).map { i ->
            (1..width).map { j -> Cell(i, j) }
        }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        val column = cellMatrix.getOrNull(i - 1)
        return column?.getOrNull(j - 1)
    }

    override fun getCell(i: Int, j: Int): Cell {
        val cell = getCellOrNull(i, j)
        require(cell != null)

        return cell
    }

    override fun getAllCells(): Collection<Cell> {
        return cellMatrix.flatten()
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        val row = cellMatrix[i - 1]
        return jRange.mapNotNull {
            row.getOrNull(it - 1)
        }
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        val rows = iRange.mapNotNull { idx ->
            cellMatrix.getOrNull(idx - 1)
        }

        return rows.mapNotNull {
            it.getOrNull(j - 1)
        }
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        val newCoordinate = when (direction) {
            Direction.UP -> Pair(i - 1, j)
            Direction.DOWN -> Pair(i + 1, j)
            Direction.LEFT -> Pair(i, j - 1)
            Direction.RIGHT -> Pair(i, j + 1)
        }
        return getCellOrNull(newCoordinate.first, newCoordinate.second)
    }
}

class GameBoardImpl<T>(width: Int) : SquareBoardImpl(width), GameBoard<T> {

    private val cellMap = mutableMapOf<Cell, T?>()

    init {
        val allCells = getAllCells()
        allCells.forEach {
            cellMap[it] = null
        }
    }

    override fun get(cell: Cell): T? {
        val c = getAllCells().firstOrNull { it == cell }
        if (c === null) {
            return null
        }
        return cellMap[c]
    }

    override fun set(cell: Cell, value: T?) {
        cellMap[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return cellMap.mapNotNull {
            if (predicate(it.value)) {
                it.key
            } else {
                null
            }
        }
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        listOf(1, 2, 4).map { it }
        cellMap.forEach {
            if (predicate(it.value)) {
                return it.key
            }
        }
        return null
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return cellMap.values.any(predicate)
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return cellMap.values.all(predicate)
    }

}