package day03

import filehelper.FileHelper

fun main(args: Array<String>) {
    println("Part One: ${partOne()}")
    println("Part Two: ${partTwo()}")
}

fun parseInput(): List<Claim> {
    val input = FileHelper.readFile("03.txt").filter { it.isNotEmpty() }
    val destructuredRegex = "#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)".toRegex()
    return input.map {
        destructuredRegex.matchEntire(it)?.destructured?.let {
            (id, left, right, width, height) ->
            Claim(id.toInt(), left.toInt(), right.toInt(), width.toInt(), height.toInt())
        } ?: error("Malformatted input.")
    }
}

fun partOne(): Int {
    val claims = parseInput()
    val sheet = Sheet()
    claims.forEach {
        sheet.claim(it)
    }
    return sheet.inchesMulticlaimed()
}

fun partTwo(): Int /* id */ {
    val claims = parseInput()
    val sheet = Sheet()
    claims.forEach {
        sheet.claim(it)
    }
    return claims.find { !sheet.checkHasOverlap(it) }?.id ?: error("No non-overlapping claim was found in input.")
}

data class Claim(val id: Int, val left: Int, val top: Int, val width: Int, val height: Int)

class Sheet {
    //x,y coordinates of the square, and the number of times it has been claimed
    val claimedSquares = mutableMapOf<Pair<Int, Int>, Int>()
    fun claim(c: Claim) {
        for(x in 0 until c.width) {
            for(y in 0 until c.height) {
                claimedSquares.compute(Pair(x + c.left, y + c.top)) { _, b ->
                    (b ?: 0) + 1
                }
            }
        }
    }

    fun inchesMulticlaimed(): Int {
        return claimedSquares.count { it.value > 0 }
    }

    fun checkHasOverlap(c: Claim): Boolean {
        for(x in 0 until c.width) {
            for(y in 0 until c.height) {
                if(claimedSquares[Pair(x + c.left, y + c.top)] != 1) return true
            }
        }
        return false
    }
}