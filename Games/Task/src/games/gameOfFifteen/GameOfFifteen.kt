package games.gameOfFifteen

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game = GameOfFifteen(initializer)


class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {

    private val gameBoard = createGameBoard<Int?>(4)

    override fun initialize() {
        val cells = gameBoard.getAllCells()
        val permutation = initializer.initialPermutation
        cells.forEachIndexed { index, item ->
            gameBoard[item] = permutation.getOrNull(index)
        }
    }

    override fun canMove(): Boolean = true

    override fun hasWon(): Boolean {
        val solved = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, null)
        val values = gameBoard.getAllCells().map { gameBoard[it] }
        return values == solved
    }

    override fun processMove(direction: Direction) {
        val emptyCell = gameBoard.find { it == null }!!

        when (direction) {
            Direction.UP -> {
                with(gameBoard) {
                    emptyCell.getNeighbour(Direction.DOWN)?.let {
                        swap(emptyCell, it)
                    }
                }
            }
            Direction.DOWN -> {
                with(gameBoard) {
                    emptyCell.getNeighbour(Direction.UP)?.let {
                        swap(emptyCell, it)
                    }
                }
            }
            Direction.LEFT -> {
                with(gameBoard) {
                    emptyCell.getNeighbour(Direction.RIGHT)?.let {
                        swap(emptyCell, it)
                    }
                }
            }
            Direction.RIGHT -> {
                with(gameBoard) {
                    emptyCell.getNeighbour(Direction.LEFT)?.let {
                        swap(emptyCell, it)
                    }
                }
            }
        }
    }

    override fun get(i: Int, j: Int): Int? {
        val cell = gameBoard.getCell(i, j)
        return gameBoard[cell]
    }

    private fun swap(a: Cell, b: Cell) {
        val value1 = gameBoard[a]
        val value2 = gameBoard[b]

        gameBoard[b] = value1
        gameBoard[a] = value2

    }
}