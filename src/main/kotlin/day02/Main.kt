package day02

import filehelper.FileHelper

fun main(args: Array<String>) {
    println("Resulting Checksum: ${partOne()}")
    println("Common letters in IDs: ${partTwo()}")
}

fun getIds() = FileHelper.readFile("02.txt").filter { it.isNotEmpty() }

fun partOne(): Int {
    val twoOfAKind = getIds().filter { hasNumOfAKind(it, 2) }.count()
    val threeOfAKind = getIds().filter { hasNumOfAKind(it,3) }.count()
    println("2 of a kind: $twoOfAKind")
    println("3 of a kind: $threeOfAKind")
    return twoOfAKind * threeOfAKind
}

fun partTwo(): String {
    getIds().forEach { a ->
        val dist = getIds().filter { b -> hammingDistance(a,b) == 1 }.firstOrNull()
        dist?.let {
            println("Hamming distance between $a and $dist is 1.")
            return commonSubstring(a, dist)
        }
    }
    error("Unreachable")
}

fun commonSubstring(a: String, b: String): String {
    return a.zip(b).filter { it.first == it.second }.map { it.first }.joinToString("")
}

fun hasNumOfAKind(id: String, num: Int): Boolean {
    return id.groupBy { it }.mapValues { vals -> vals.value.count() }.any { it.value == num }
}

fun hammingDistance(a: String, b: String): Int {
    return a.zip(b).filter { it.first != it.second }.count()
}