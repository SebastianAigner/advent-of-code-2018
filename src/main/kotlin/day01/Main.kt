package day01

import filehelper.FileHelper

fun main(args: Array<String>) {
    println("Resulting frequency is ${partOne()}")
    println("First frequency that repeats is ${partTwoSimple()} (simple.)")
    println("First frequency that repeats is ${partTwo()}")
}

fun getInts() = FileHelper.readFile("01.txt").filter { it.isNotEmpty() }.map { it.toInt() }

fun partOne() = getInts().sum()

fun partTwoSimple(): Int {
    val encounteredFrequencies = mutableSetOf<Int>()
    val ints = getInts()
    var ctr = 0
    while(true) {
        for(f in ints) {
            ctr += f
            if(ctr in encounteredFrequencies) return ctr else encounteredFrequencies += ctr
        }
    }
}

fun partTwo(): Int {
    val encounteredFrequencies = mutableSetOf<Int>()
    val ints = CircularList(getInts())
    var ctr = 0
    for (f in ints) {
        ctr += f
        if(ctr in encounteredFrequencies) return ctr else encounteredFrequencies += ctr
    }
    throw RuntimeException("Unreachable") // this is not nice, but required to guarantee a return value for the compiler.
}

class CircularList<T>(val list: List<T>): Iterable<T> {
    var pos = 0
    override fun iterator(): Iterator<T> {
        return object: Iterator<T> {
            override fun hasNext(): Boolean {
                return list.isNotEmpty()
            }
            override fun next(): T {
                if(pos == list.size) {
                    pos = 0
                }
                return list[pos++]
            }
        }
    }
}