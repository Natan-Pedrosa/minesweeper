package org.example

import kotlin.random.Random
fun main() {
    val minesweeper = Minesweeper(9, 9)

    print("How many mines do you want on the field?")
    val amountMines = readln().toInt()
    minesweeper.creatGame(amountMines)
    minesweeper.show()
}

class Minesweeper(val x: Int, val y: Int){

    private val field = mutableListOf(mutableListOf("1"))
    private lateinit var coordanateMines: MutableSet<MutableList<Int>>

    fun creatGame(amountMines: Int){
        coordanateMines = setRandomMines(amountMines)

        field.clear()
        repeat(this.x){
            val lines = mutableListOf<String>()
            repeat(this.y){
                lines.add(".")
            }

            field.add(lines)
        }

        val xMines = coordanateMines.toMutableList()
        repeat(amountMines){
            val yMines = xMines[it].toMutableList()
            val x = yMines[0]
            val y = yMines[1]

            field[x][y] = "X"
        }

        setNumberMinesAroundSpace()
    }

    private fun setRandomMines(amount: Int): MutableSet<MutableList<Int>>{
        val mines = mutableSetOf(
            mutableListOf(
                Random.nextInt(0, 9),
                Random.nextInt(0, 9)))

        while (mines.size != amount){
            repeat(amount - mines.size){
                mines.add(mutableListOf(
                    Random.nextInt(0, 9),
                    Random.nextInt(0, 9)))
            }
        }

        return mines
    }

    private fun setNumberMinesAroundSpace(){
        val xMines = coordanateMines.toMutableList()

        repeat(xMines.size){
            val yMines = xMines[it].toMutableList()
            val indexX = yMines[0]
            val indexY = yMines[1]

            if(indexX - 1 >= 0 && indexY - 1 >= 0 && field[indexX - 1][indexY - 1] != "X"){
                field[indexX - 1][indexY - 1] = if (field[indexX - 1][indexY - 1] == ".")
                    "1"
                else
                    "${field[indexX - 1][indexY - 1].toInt() + 1}"
            }

            if(indexX - 1 >= 0 && field[indexX - 1][indexY] != "X"){
                field[indexX - 1][indexY] = if (field[indexX - 1][indexY] == ".")
                    "1"
                else
                    "${field[indexX - 1][indexY].toInt() + 1}"
            }

            if(indexX - 1 >= 0 && indexY + 1 < this.y && field[indexX - 1][indexY + 1] != "X"){
                field[indexX - 1][indexY + 1] = if (field[indexX - 1][indexY + 1] == ".")
                    "1"
                else
                    "${field[indexX - 1][indexY + 1].toInt() + 1}"
            }

            if(indexY - 1 >= 0 && field[indexX][indexY - 1] != "X"){
                field[indexX][indexY - 1] = if (field[indexX][indexY - 1] == ".")
                    "1"
                else
                    "${field[indexX][indexY - 1].toInt() + 1}"
            }

            if(indexY + 1 < this.y && field[indexX][indexY + 1] != "X"){
                field[indexX][indexY + 1] = if (field[indexX][indexY + 1] == ".")
                    "1"
                else
                    "${field[indexX][indexY + 1].toInt() + 1}"
            }

            if(indexX + 1 < this.x && indexY - 1 >= 0 && field[indexX + 1][indexY - 1] != "X"){
                field[indexX + 1][indexY - 1] = if (field[indexX + 1][indexY - 1] == ".")
                    "1"
                else
                    "${field[indexX + 1][indexY - 1].toInt() + 1}"
            }

            if(indexX + 1 < this.x && field[indexX+1][indexY] != "X"){
                field[indexX + 1][indexY] = if (field[indexX + 1][indexY] == ".")
                    "1"
                else
                    "${field[indexX + 1][indexY].toInt() + 1}"
            }

            if(indexX + 1 < this.x && indexY + 1 < this.y && field[indexX+1][indexY+1] != "X"){
                field[indexX + 1][indexY + 1] = if (field[indexX + 1][indexY + 1] == ".")
                    "1"
                else
                    "${field[indexX + 1][indexY + 1].toInt() + 1}"
            }

        }

    }

    fun show(){
        field.forEach { println(it.joinToString("")) }
    }
}
