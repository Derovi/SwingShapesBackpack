package by.derovi.shapes.shapes

abstract class Shape : Comparable<Shape> {
    abstract val volume: Double

    override fun compareTo(other: Shape): Int {
        return when {
            volume < other.volume -> -1
            volume > other.volume -> 1
            else -> 0
        }
    }
}