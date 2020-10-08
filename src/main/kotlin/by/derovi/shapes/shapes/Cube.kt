package by.derovi.shapes.shapes

import by.derovi.shapes.Name

@Name("Куб")
class Cube(
    @Name("Длина стороны")
    val side: Int = 0) : Shape() {

    override val volume: Double
        get() = side.toDouble() * side.toDouble()
}
