package org.example

import kotlin.random.Random
fun main() {
    val minesweeper = Minesweeper(9, 9)

    print("How many mines do you want on the field?")
    val amountMines = readln().toInt()
    minesweeper.creatGame(amountMines)
    minesweeper.show()
    minesweeper.play()
}

class Minesweeper(val x: Int, val y: Int){

    private val field = mutableListOf(mutableListOf("1"))
    private lateinit var coordinatesMines: MutableSet<MutableList<Int>>
    private val coordinatesMarked = mutableSetOf(mutableListOf(0, 0))

    fun creatGame(amountMines: Int){
        coordinatesMines = setRandomMines(amountMines)

        field.clear()
        coordinatesMarked.clear()

        repeat(this.x){
            val lines = mutableListOf<String>()
            repeat(this.y){
                lines.add(".")
            }

            field.add(lines)
        }

        val xMines = coordinatesMines.toMutableList()
        repeat(amountMines){
            val yMines = xMines[it].toMutableList()
            val x = yMines[0]
            val y = yMines[1]

            field[x][y] = "X"
        }

        setNumberAroundMines()
        hideMines()
    }

    fun play(){

        while (true){
            print("Set/delete mines marks (x and y coordinates):")
            // nao percebi a referencia as cordenadas x e y, troquei a ordem quando criado
            val (y, x) = readln().split(" ").map { it.toInt() - 1 }

            when{
                mutableListOf(x, y) in coordinatesMines -> {
                    if (field[x][y] == "."){

                        field[x][y] = "*"
                        coordinatesMarked.add(mutableListOf(x, y))
                        show()
                        if (coordinatesMarked.size == coordinatesMines.size)
                            if (coordinatesMarked == coordinatesMines)
                                break

                    }else {

                        field[x][y] = "."
                        coordinatesMarked.remove(mutableListOf(x, y))
                        show()
                    }
                }
                field[x][y] == "." -> {
                    field[x][y] = "*"
                    coordinatesMarked.add(mutableListOf(x, y))
                    show()
                }
                field[x][y] == "*" ->{
                    field[x][y] = "."
                    coordinatesMarked.remove(mutableListOf(x, y))
                    show()
                }
                isNumeric(field[x][y]) -> println("There is a number here!")
            }
        }

        println("Congratulations! You found all the mines!")
        coordinatesMarked.clear()
    }

    fun show(){


        print(" |")
        repeat(field.size){
            print("${it + 1}")
        }
        println("|")

        print("-|")
        repeat(field.size){
            print("-")
        }
        println("|")

        var index = 0
        for (line in field){

            println("${++index}|${line.joinToString("")}|")
        }

        print("-|")
        repeat(field.size){
            print("-")
        }
        println("|")
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

    private fun setNumberAroundMines(){
        val xMines = coordinatesMines.toMutableList()

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

    private fun hideMines(){
        val xMines = coordinatesMines.toMutableList()

        repeat(xMines.size) {
            val yMines = xMines[it].toMutableList()
            val indexX = yMines[0]
            val indexY = yMines[1]

            field[indexX][indexY] = "."
        }
    }
    private fun isNumeric(toCheck: String): Boolean {
        return toCheck.all { char -> char.isDigit() }
    }
}
