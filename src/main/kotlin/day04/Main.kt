package day04

import filehelper.FileHelper
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

fun main(args: Array<String>) {
    println("Part 1: ${partOne()}")
    println("Part 2: ${partTwo()}")
}

fun parseInput(): List<LogbookEntry> {
    val reg = "\\[1518-(\\d+)-(\\d+) (\\d+):(\\d+)] (.+)".toRegex()
    return FileHelper.readFile("04.txt").filter { it.isNotEmpty() }.map {
        reg.matchEntire(it)?.destructured?.let { (mo, d, h, mi, b) ->
            LogbookEntry(mo.toInt(), d.toInt(), h.toInt(), mi.toInt(), b)
        } ?: error("Malformatted Input")
    }.sortedWith(compareBy({ it.month }, { it.day }, { it.hour }, { it.minute }))
}

fun partOne() {
    var currentGuard: Int? = null
    var asleepAt: LocalDateTime? = null
    for (entry in parseInput()) {
        when (val mb = entry.getMessageBody()) {
            is ShiftMessage -> {
                currentGuard = mb.guardId
            }
            is SleepMessage -> {
                asleepAt = LocalDateTime.of(1518, entry.month, entry.day, entry.hour, entry.minute)
            }
            is WakeMessage -> {
                val asleepTill = LocalDateTime.of(
                    2018,
                    entry.month,
                    entry.day,
                    entry.hour,
                    entry.minute - 1
                ) // since they are already counted as awake.
                if (currentGuard != null && asleepAt != null) {
                    Timetable.logSleep(currentGuard, asleepAt, asleepTill)
                }
            }
        }
    }
    val sleepyGuard = Timetable.getMostSleepingGuard()
    val ret = Timetable.findMostSleptMinute(sleepyGuard)
    println("Most slept: #$sleepyGuard @ $ret")
}

fun partTwo() = Timetable.mostFrequentMinuteChecksum()

object Timetable {
    private val sleepTimes = mutableMapOf<Int, MutableList<Pair<LocalDateTime, LocalDateTime>>>()

    fun logSleep(id: Int, at: LocalDateTime, till: LocalDateTime) {
        sleepTimes.compute(id) { _, b ->
            (b ?: mutableListOf()).apply { add(Pair(at, till)) }
        }
    }

    fun getMostSleepingGuard(): Int { //returns the ID of the elf.
        println(sleepTimes)
        val most = sleepTimes.toList().sortedByDescending { a ->
            val sleeptimes = a.second
            val seconds = sleeptimes.map {
                val secondsAsleep = it.first.until(it.second, ChronoUnit.SECONDS)
                secondsAsleep
            }.sum()
            seconds
        }.firstOrNull()
        return most?.first ?: error("No guard that has slept the most.")
    }

    fun findMostSleptMinute(id: Int): Pair<Pair<Int, Int>, Int> {
        val l = sleepTimes[id] ?: error("Guard #$id does not exist.")
        val minutes = mutableMapOf<Pair<Int, Int>, Int>() // (h,m) -> cnt
        l.forEach {
            val endTime = it.second.toLocalTime()
            var timeCtr = it.first.toLocalTime()
            do {
                minutes.compute(Pair(timeCtr.hour, timeCtr.minute)) { _, cnt ->
                    (cnt ?: 0) + 1
                }
                timeCtr = timeCtr.plusMinutes(1)
            } while (timeCtr <= endTime)
        }
        val top = minutes.toList().maxBy { it.second }
        return top ?: error("broken")
    }

    fun mostFrequentMinuteChecksum(): Int {
        val stuff = sleepTimes
            .keys
            .map {
                Pair(it, findMostSleptMinute(it))
            }
            .maxBy { it.second.second }
        println("superfrequent = $stuff")
        val id = stuff?.first ?: error("broken")
        val minute = stuff.second.first.second
        return id * minute
    }
}

data class LogbookEntry(val month: Int, val day: Int, val hour: Int, val minute: Int, val body: String) {
    companion object {
        val sleepReg = "falls".toRegex()
        val wakeReg = "wakes".toRegex()
        val shiftReg = ".+#(\\d+).+".toRegex()
    }

    fun shift() = shiftReg.matchEntire(body)?.destructured?.let { (i) -> i.toInt() }

    fun getMessageBody(): MessageBody {
        if (sleepReg.containsMatchIn(body)) return SleepMessage
        if (wakeReg.containsMatchIn(body)) return WakeMessage
        shift()?.let {
            return ShiftMessage(it)
        }
    }
}

sealed class MessageBody
class ShiftMessage(val guardId: Int) : MessageBody()
object SleepMessage : MessageBody()
object WakeMessage : MessageBody()
