package by.derovi.shapes.shapes

import java.util.*

abstract class Shape : Comparable<Shape> {
    abstract val volume: Double

    val id = Random().nextInt()

    override fun compareTo(other: Shape): Int {
        return when {
            volume < other.volume -> -1
            volume > other.volume -> 1
            else -> 0
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Shape) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}