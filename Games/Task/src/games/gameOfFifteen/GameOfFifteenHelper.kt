package games.gameOfFifteen

/*
 * This function should return the parity of the permutation.
 * true - the permutation is even
 * false - the permutation is odd
 * https://en.wikipedia.org/wiki/Parity_of_a_permutation
 * If the game of fifteen is started with the wrong parity, you can't get the correct result
 *   (numbers sorted in the right order, empty cell at last).
 * Thus the initial permutation should be correct.
 */
fun isEven(permutation: List<Int>): Boolean {
    fun isInverted(pair: Pair<Int, Int>): Boolean = pair.first > pair.second

    var inversionCount = 0
    permutation.forEachIndexed lit@{ index, firstElement ->
        if (index == permutation.lastIndex) {
            return@lit
        }
        val restOfTheList = permutation.subList(index + 1, permutation.size)
        inversionCount += restOfTheList.count { it < firstElement }
    }
    return (inversionCount % 2) == 0

}