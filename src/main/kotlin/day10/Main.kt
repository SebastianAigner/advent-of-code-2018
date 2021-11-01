package day10

import filehelper.FileHelper
import java.awt.Color
import java.awt.FlowLayout
import java.awt.image.BufferedImage
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JLabel

fun main(args: Array<String>) {
    partOne()
}

fun partOne() {
    val bufImg = BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB)
    val clear = BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB)

    val input = FileHelper.readFile("10_mini.txt").filter { it.isNotEmpty() }
    val destructuredRegex = ".*([- ]?\\d+).*([- ]\\d+).*([- ]\\d+).*([- ]\\d+)>".toRegex()
    val stuff = input.map {
        println(it)
        destructuredRegex.matchEntire(it)?.destructured?.let { (posX, posY, velX, velY) ->
            val part = Particle(posX.trim().toInt(), posY.trim().toInt(), velX.trim().toInt(), velY.trim().toInt())
            println(part)
            part
        } ?: error("Malformatted input.")
    }


    val jf = JFrame()
    jf.contentPane.layout = FlowLayout()
    val jlabel = JLabel(ImageIcon(bufImg))
    jf.contentPane.add(JLabel(ImageIcon(bufImg)))
    jf.pack()
    jf.isVisible = true

    var hasDrawn = false
    while (true) {

        if (hasDrawn) {
            jf.invalidate()
            jf.repaint()
            Thread.sleep(1000)
            hasDrawn = false
        }
        bufImg.data = clear.raster
        stuff.forEach {
            it.step()
            if (it.posX + 500 in 1..1000 && it.posY + 500 in 1..1000) {
                val g = bufImg.graphics
                g.color = Color.red
                g.fillRect(it.posX + 500, it.posY + 500, 3, 3)
                hasDrawn = true
            }
        }
    }
}

data class Particle(var posX: Int, var posY: Int, val velX: Int, val velY: Int) {
    fun step() {
        posX += velX
        posY += velY
    }
}