package games.game2048

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Your task is to implement the game 2048 https://en.wikipedia.org/wiki/2048_(video_game).
 * Implement the utility methods below.
 *
 * After implementing it you can try to play the game running 'PlayGame2048'.
 */
fun newGame2048(initializer: Game2048Initializer<Int> = RandomGame2048Initializer): Game =
    Game2048(initializer)

class Game2048(private val initializer: Game2048Initializer<Int>) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        repeat(2) {
            board.addNewValue(initializer)
        }

        val k = mutableListOf(1)
        with(k) {
            this.add(23)
        }

        k.shuffle() {
            this.removeAll { true }
        }
        println(k)
    }

    fun MutableList<Int>.shuffle(actions: MutableList<Int>.() -> Unit) {
        actions(this)

    }

    override fun canMove() = board.any { it == null }

    override fun hasWon() = board.any { it == 2048 }

    override fun processMove(direction: Direction) {
        if (board.moveValues(direction)) {
            board.addNewValue(initializer)
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}

/*
 * Add a new value produced by 'initializer' to a specified cell in a board.
 */
fun GameBoard<Int?>.addNewValue(initializer: Game2048Initializer<Int>) {
    initializer.nextValue(this)?.let {
        this[it.first] = it.second
    }
}

/*
 * Update the values stored in a board,
 * so that the values were "moved" in a specified rowOrColumn only.
 * Use the helper function 'moveAndMergeEqual' (in Game2048Helper.kt).
 * The values should be moved to the beginning of the row (or column),
 * in the same manner as in the function 'moveAndMergeEqual'.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValuesInRowOrColumn(rowOrColumn: List<Cell>): Boolean {

    // Current values
    val currentValues = rowOrColumn.map { this[it] }

    // Transform the values
    val movedValues = rowOrColumn
        .map { this[it] }
        .moveAndMergeEqual { it * 2 }

    // Update the values
    rowOrColumn.forEachIndexed { index, cell ->
        val k = movedValues.getOrNull(index)
        this[cell] = k
    }
    println("  Previous values: $currentValues")
    println("  Moved values: $movedValues")
    return currentValues != movedValues && currentValues.any { it != null }
}

/*
 * Update the values stored in a board,
 * so that the values were "moved" to the specified direction
 * following the rules of the 2048 game .
 * Use the 'moveValuesInRowOrColumn' function above.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValues(direction: Direction): Boolean {
    val cellsForIndex: (Int) -> List<Cell> = when (direction) {
        Direction.RIGHT -> {
            { this.getRow(it, 1..width).reversed() }
        }
        Direction.LEFT -> {
            { this.getRow(it, 1..width) }
        }
        Direction.DOWN -> {
            { this.getColumn(1..width, it).reversed()}
        }
        Direction.UP -> {
            { this.getColumn(1..width, it) }
        }
    }
    val moveResults = (1..width).map {
        println("Direction: ${direction} Iteration: $it")
        val row = cellsForIndex(it)
        println("Cells: ${row}")
        moveValuesInRowOrColumn(row)
    }
    println("Results: ${moveResults}")
    return moveResults.any { it }


}