package org.example

fun main() {
    val minesweeper = Minesweeper()
    minesweeper.creatGame()
    minesweeper.show()
}

class Minesweeper(){

    private val board = mutableListOf(mutableListOf(".", ".", "X", "."))

    fun creatGame(){
        val line1 = mutableListOf(".", ".", ".", "X")
        val line2 = mutableListOf(".", "X", ".", ".")
        val line3 = mutableListOf("X", ".", ".", ".")

        board.add(line1)
        board.add(line2)
        board.add(line3)
    }

    fun show(){
        board.forEach { println(it.joinToString(" ")) }
    }
}
