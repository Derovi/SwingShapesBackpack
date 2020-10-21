package by.derovi.shapes

import by.derovi.shapes.shapes.Shape
import java.lang.Exception
import java.util.*

class Backpack<T : Shape>(var size: Int) {
    class BackPackOverflowException(size: Int) : Exception("Back pack overflow! (Size: $size)")

    val shapes: MutableSet<T> = TreeSet<T> { sh1, sh2 -> if (sh1.volume == sh2.volume && !(sh1 === sh2)) 1 else (sh2.volume - sh1.volume).toInt() }

    fun add(shape: T) {
        if (shapes.size == size) {
            throw BackPackOverflowException(size)
        }
        shapes.add(shape)
    }
}
