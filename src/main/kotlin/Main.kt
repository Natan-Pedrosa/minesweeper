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

    private val field = mutableListOf<MutableList<String>>()
    private lateinit var coordinatesMines: MutableSet<MutableList<Int>>
    private val coordinatesNumbers = mutableMapOf<String, Int>()
    private val coordinatesMarked = mutableSetOf<MutableList<Int>>()
    private var amountMines = 0

    fun creatGame(amountMines: Int){
        this.amountMines = amountMines

        //coordinatesMines = setRandomMines(amountMines)
        // test 1
        /*coordinatesMines = mutableSetOf(
            mutableListOf(0, 6),
            mutableListOf(1, 0),
            mutableListOf(2, 5),
            mutableListOf(5, 1),
            mutableListOf(5, 4),
            mutableListOf(6, 2),
            mutableListOf(6, 6),
            mutableListOf(7, 1),
            mutableListOf(7, 4),
            mutableListOf(8, 3)
        )*/
        // test 2
        /*coordinatesMines = mutableSetOf(
            mutableListOf(0, 1),
            mutableListOf(0, 6),
            mutableListOf(0, 8),
            mutableListOf(2, 6),
            mutableListOf(5, 7),
            mutableListOf(6, 2),
            mutableListOf(7, 0),
            mutableListOf(7, 1)
        )*/
        // 3
       /*coordinatesMines = mutableSetOf(
            mutableListOf(2, 6),
            mutableListOf(3, 1),
            mutableListOf(4, 8),
            mutableListOf(5, 6),
            mutableListOf(8, 1)
        )*/
        /*coordinatesMines = mutableSetOf(
            mutableListOf(2, 6),
            mutableListOf(3, 1)
        )*/
        /*coordinatesMines = mutableSetOf(
            mutableListOf(3, 8),
            mutableListOf(5, 5)
        )*/
        coordinatesMines = mutableSetOf(
            mutableListOf(0, 3),
            mutableListOf(2, 7)
        )
        setEmptyCells()
        setNumberAroundMines()
        /*coordinatesNumbers.forEach {
            val x = it.key[0].toString().toInt()
            val y = it.key[1].toString().toInt()

            field[x][y] = it.value.toString()
        }*/
        // hideMines()
    }

    private fun setEmptyCells() {

        repeat(this.x){
            val lines = mutableListOf<String>()
            repeat(this.y){
                lines.add(".")
            }

            field.add(lines)
        }
    }

    fun play(){
        var firstFree = true

        while (true){
            print("Set/unset mines marks or claim a cell as free:")
            // nao percebi a referencia as coordenadas x e y, troquei a ordem quando criado
            val (strY, strX, action) = readln().split(" ")
            val x = strX.toInt() - 1
            val y = strY.toInt() - 1

            when(action){
                "mine" -> {
                    if (field[x][y] == "."){

                        field[x][y] = "*"
                        coordinatesMarked.add(mutableListOf(x, y))

                        if (coordinatesMarked.size == coordinatesMines.size) {
                            if (coordinatesMarked == coordinatesMines) {

                                show()
                                println("Congratulations! You found all the mines!")
                                break
                            }
                        }

                        show()
                    }else if(field[x][y] == "*"){

                        field[x][y] = "."
                        coordinatesMarked.remove(mutableListOf(x, y))
                        show()
                    }
                }
                "free" -> {
                    val possibleMine = mutableListOf(x, y)

                    when{
                        possibleMine in coordinatesMines -> {
                            if(firstFree){
                                field[x][y] = "."

                                while(possibleMine in coordinatesMines){
                                    coordinatesMines.remove(possibleMine)

                                    val xNewMine = Random.nextInt(0, 8)
                                    val yNewMine = Random.nextInt(0, 8)

                                    coordinatesMines.add(mutableListOf(xNewMine, yNewMine))
                                }

                                setNumberAroundMines()

                                /*coordinatesMines.forEach {
                                    val xX = it[0].toString().toInt()
                                    val yY = it[1].toString().toInt()

                                    field[xX][yY] = "X"
                                }*/

                                if(coordinatesNumbers.contains("${x}${y}")){
                                    field[x][y] = coordinatesNumbers["${x}${y}"].toString()
                                    show()

                                    if(getAmountFreeSpace() == amountMines){
                                        println("Congratulations! You found all the mines!")
                                        break
                                    }

                                }else{
                                    exploreEmptyCell(Pair(x, y))
                                    exploreEmptyCellTwo(Pair(x, y))
                                    show()

                                    if(getAmountFreeSpace() == amountMines){
                                        println("Congratulations! You found all the mines!")
                                        break
                                    }
                                }
                            }else{
                                setAllMines()
                                show()
                                println("You stepped on a mine and failed!")
                                break
                            }
                        }
                        coordinatesNumbers.contains("${x}${y}") -> {
                            field[x][y] = coordinatesNumbers["${x}${y}"].toString()
                            show()

                            if(getAmountFreeSpace() == amountMines){
                                println("Congratulations! You found all the mines!")
                                break
                            }
                        }
                        field[x][y] == "." -> {
                            exploreEmptyCell(Pair(x, y))
                            exploreEmptyCellTwo(Pair(x, y))
                            show()

                            if(getAmountFreeSpace() == amountMines){
                                println("Congratulations! You found all the mines!")
                                break
                            }
                        }
                    }

                    firstFree = false
                }
            }
        }

        coordinatesMarked.clear()
    }
    private fun getAmountFreeSpace(): Int{
        var count = 0

        repeat(9){x->
            repeat(9) { y ->
                if (field[x][y] == ".")
                    ++count
            }
        }

        return count
    }

    private fun exploreEmptyCellTwo(pair: Pair<Int, Int>){
        field[pair.first][pair.second] = "/"

        exploreAroundExploredCell(pair)

        for (row in pair.first .. 8){
            for (col in 0 .. 8){
                exploreAroundExploredCell(Pair(row, col))
            }
            for (col in 8 downTo  0){
                exploreAroundExploredCell(Pair(row, col))
            }
        }

        for (row in 8 downTo 0){
            for (col in 8 downTo 0){
                exploreAroundExploredCell(Pair(row, col))
            }

            for (col in 0 .. 8){
                exploreAroundExploredCell(Pair(row, col))
            }
        }

        for (row in 0 .. 8){
            for (col in 0 .. 8){
                exploreAroundExploredCell(Pair(row, col))
            }
            for (col in 8 downTo  0){
                exploreAroundExploredCell(Pair(row, col))
            }
        }
    }

    private fun exploreEmptyCell(pair: Pair<Int, Int>) {
        field[pair.first][pair.second] = "/"
        var amountExploredCell = exploreAroundExploredCell(pair)

        if(amountExploredCell == 0)
            return

        var layer = 0

        do {

            amountExploredCell = explorerLayer(pair,++layer)
        }while(amountExploredCell != 0)
    }

    private fun explorerLayer(pair: Pair<Int, Int>, layer: Int): Int {
        // layer represent a square

        val lengthSideSquare = 3 + (layer - 1) * 2
        val k = lengthSideSquare - 1
        val amountCellToExplore = layer * 8

        val startSideOne = 1
        val endSideOne = startSideOne + k
        val startSideTwo = endSideOne
        val endSideTwo = startSideTwo + k
        val startSideThree = endSideTwo
        val endSideThree = startSideThree + k
        val startSideFour = endSideThree
        val endSideFour = startSideFour + k

        var amountExploredCellTotal = 0

        repeat(3){
            var x =  pair.first - layer
            var y = pair.second - layer

            repeat(amountCellToExplore){index ->

                amountExploredCellTotal += exploreAroundExploredCell(Pair(x, y))

                when(index + 1){
                    in startSideOne until endSideOne -> ++x
                    in startSideTwo until endSideTwo -> ++y
                    in startSideThree until endSideThree -> --x
                    in startSideFour until endSideFour -> --y
                }
            }
            show()
        }

        return amountExploredCellTotal
    }

    private fun exploreAroundExploredCell(pair: Pair<Int, Int>): Int{

        if(pair.first !in 0 ..8 ||
            pair.second !in 0 .. 8 ||
            field[pair.first][pair.second] != "/")
            return 0

        var x = pair.first - 1
        var y = pair.second - 1

        var amountExploredCell = 0

        repeat(8){index ->
            if (mutableListOf(x, y) in coordinatesMines)
            else if (coordinatesNumbers.contains("$x$y")){
                field[x][y] = coordinatesNumbers["$x$y"].toString()
            }else if(x in 0 ..8 &&
                    y in 0.. 8 &&
                    field[x][y] == ".")
            {
                field[x][y] = "/"
                ++amountExploredCell
            }else if(x in 0 ..8 &&
                    y in 0.. 8 &&
                    field[x][y] == "*")
            {
                coordinatesMarked.remove(mutableListOf(x, y))

                field[x][y] = "/"
                ++amountExploredCell
            }

            when(index + 1){
                in 1 until 3 -> ++x
                in 3 until 5 -> ++y
                in 5 until 7 -> --x
                in 7 until 9 -> --y
            }
        }

        return amountExploredCell
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

    private fun setAllMines(){
        val iterator = coordinatesMines.iterator()
        repeat(coordinatesMines.size) {
            val coordinates = iterator.next()
            val x = coordinates[0]
            val y = coordinates[1]

            field[x][y] = "X"
        }
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

        coordinatesNumbers.clear()
        repeat(xMines.size){
            val yMines = xMines[it].toMutableList()
            var indexX = yMines[0] - 1
            var indexY = yMines[1] - 1

            repeat(8){itN ->
                if (
                    indexX in 0 ..8 &&
                    indexY in 0.. 8 &&
                    mutableListOf(indexX, indexY) !in coordinatesMines)
                {
                    val key = "${indexX}${indexY}"
                    val value = coordinatesNumbers[key] ?: 0
                    coordinatesNumbers[key] = value + 1
                }

                when(itN + 1){
                    in 1 until 3 -> ++indexX
                    in 3 until 5 -> ++indexY
                    in 5 until 7 -> --indexX
                    in 7 until 9 -> --indexY
                }
            }
        }
    }

    /*private fun hideMines(){
        val xMines = coordinatesMines.toMutableList()

        repeat(xMines.size) {
            val yMines = xMines[it].toMutableList()
            val indexX = yMines[0]
            val indexY = yMines[1]

            field[indexX][indexY] = "."
        }
    }*/
    private fun isNumeric(toCheck: String): Boolean {
        return toCheck.all { char -> char.isDigit() }
    }
}
