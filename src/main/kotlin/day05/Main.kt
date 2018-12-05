package day05

import filehelper.FileHelper
import javax.xml.bind.JAXBElement

fun main() {
    println(partOne())
    println(partTwo())
}

fun partOne(): Int {
    val polymer = FileHelper.readFile("05.txt").first()
    val miniPoly = collapsePoly(polymer)
    return miniPoly.count()
}

fun partTwo(): Pair<Char, Int> {
    val poly = FileHelper.readFile("05.txt").first()
    val keepingCount = mutableMapOf<Char, Int>()
    ('a'..'z').forEach { charToClean ->
        val cleaned = poly.filter { it.toLowerCase() != charToClean }
        val len = collapsePoly(cleaned).count()
        keepingCount[charToClean] = len
        println("Cleaning '$charToClean' results in poly length of $len")
    }
    return keepingCount.toList().sortedBy { it.second }.first()
}

tailrec fun collapsePoly(p: String): String {
    val shrunk = shrinkPolymer(p)
    if(shrunk.length % 100 == 0) println("New length: ${shrunk.length}")
    return if(p.length == shrunk.length) p
    else collapsePoly(shrunk)
}

fun shrinkPolymer(poly: String): String {
    val sb = StringBuilder()
    var idx = 0
    while(idx <= poly.lastIndex) {
        val firstChar = poly[idx]
        if(poly.lastIndex == idx) {
            //this means we have the last remaining letter, that hasn't been removed, presumably.
            sb.append(firstChar)
            ++idx
            continue
        }
        val secondChar = poly[idx+1]
        if(firstChar.isLowerCase() && secondChar.isUpperCase() && firstChar.toLowerCase() == secondChar.toLowerCase()) {
            ++idx //skip next
            //println("Matching, skipping next!")
            //do not add to new string
        } else if (firstChar.isUpperCase() && secondChar.isLowerCase() && firstChar.toLowerCase() == secondChar.toLowerCase()) {
            ++idx
            //do not add to new string
        } else {
            //add to new string
            sb.append(firstChar)
        }
        //fill
        ++idx
    }
    //println("New Polymer: $newPolymer")
    return sb.toString()
}