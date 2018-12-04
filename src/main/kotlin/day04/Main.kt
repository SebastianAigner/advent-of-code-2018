package day04

import filehelper.FileHelper
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

fun main(args: Array<String>) {
    println("Part 1: ${partOne()}")
    println("Part 2: ${partTwo()}")
}

fun parseInput(): List<LogbookEntry> {
    val reg = "\\[1518-(\\d+)-(\\d+) (\\d+):(\\d+)\\] (.+)".toRegex()
    return FileHelper.readFile("04.txt").filter { it.isNotEmpty() }.map {
        reg.matchEntire(it)?.destructured?.let {
            (mo,d,h,mi,b) ->
            LogbookEntry(mo.toInt(), d.toInt(), h.toInt(), mi.toInt(), b)
        } ?: error("Malformatted Input")
    }.sortedWith(compareBy({ it.month }, { it.day }, { it.hour }, { it.minute } ))
}

fun partOne() {
    var currentGuard: Int? = null
    var asleepAt: LocalDateTime? = null
    for(entry in parseInput()) {
        when(val x = entry.getMessageBody()) {
            is ShiftMessage -> {
                currentGuard = x.guardId
            }
            is SleepMessage -> {
                asleepAt = LocalDateTime.of(1518, entry.month, entry.day, entry.hour, entry.minute)
            }
            is WakeMessage -> {
                val asleepTill = LocalDateTime.of(2018, entry.month, entry.day, entry.hour, entry.minute-1) //todo: since they are already counted as awake.
                Timetable.logSleep(currentGuard ?: error("no guard."), asleepAt ?: error("never fell asleep."), asleepTill)
            }
        }
    }
    val sleepyGuard = Timetable.getMostSleepingGuard()
    println("Most sleepy: $sleepyGuard")
    Timetable.findMostSleptMinute(sleepyGuard)
}

fun partTwo() = Timetable.mostFrequentMinuteChecksum()

object Timetable {
    val sleepTimes = mutableMapOf<Int, MutableList<Pair<LocalDateTime, LocalDateTime>>>()
    fun logSleep(id: Int, asleepAt: LocalDateTime, asleepTill: LocalDateTime) {
        val thing = sleepTimes.getOrDefault(id, mutableListOf())
        thing += Pair(asleepAt, asleepTill)
        sleepTimes[id] = thing
    }

    fun getMostSleepingGuard(): Int { //returns the ID of the elf.
        println(sleepTimes)
        val most =  sleepTimes.toList().sortedByDescending { a ->
            val sleeptimes = a.second
            val seconds = sleeptimes.map {
                val secondsAsleep = it.first.until(it.second, ChronoUnit.SECONDS)
                secondsAsleep
            }.sum()
            seconds
        }.firstOrNull()
        println("shleepyboi" + most)
        return most?.first ?: error("not found")
    }

    fun findMostSleptMinute(id: Int): Pair<Pair<Int, Int>, Int> {
        val l = sleepTimes[id] ?: error("damn")
        val minutes = mutableMapOf<Pair<Int, Int>, Int>() // (h,m) -> cnt
        l.forEach {
            val currentTime = it.first.toLocalTime()
            val endTime = it.second.toLocalTime()
            var timeCtr = currentTime
            do {
                minutes.compute(Pair(timeCtr.hour, timeCtr.minute)) {
                    _, b ->
                    (b ?: 0) + 1
                }
                timeCtr = timeCtr.plusMinutes(1)
                println(timeCtr)
            } while(timeCtr <= endTime)
            println("done with one mapping.")
        }
        println("Sleepmaster: $minutes")
        val top = minutes.toList().maxBy { it.second }
        println(
            "top shit: $top"
        )
        return top ?: error("broken")
    }

    fun mostFrequentMinuteChecksum(): Int {
        val stuff = sleepTimes.keys.map {
            Pair(it, findMostSleptMinute(it))
        }.maxBy { it.second.second }
        println("superfrequent = $stuff")
        val id = stuff?.first ?: error("broken")
        val minute = stuff.second.first.second
        return id * minute
    }
}

data class LogbookEntry(val month: Int, val day: Int, val hour: Int, val minute: Int, val body: String) {
    val shiftReg = ".+#(\\d+).+".toRegex()
    fun shift(): Int? {
        return shiftReg.matchEntire(body)?.destructured?.let {
                (i) -> println(i); i.toInt()
        }
    }

    val sleepReg = "falls".toRegex()
    fun sleep(): Boolean {
        return sleepReg.containsMatchIn(body)
    }

    val wakeReg = "wakes".toRegex()
    fun wake(): Boolean {
        return wakeReg.containsMatchIn(body)
    }

    fun getMessageBody(): MessageBody {
        val shift = shift()
        if(shift != null) return ShiftMessage(shift)
        if(sleep()) return SleepMessage()
        if(wake()) return WakeMessage()
        error("Unreachable: $body")
    }

}

sealed class MessageBody
class ShiftMessage(val guardId: Int): MessageBody()
class SleepMessage: MessageBody()
class WakeMessage: MessageBody()
