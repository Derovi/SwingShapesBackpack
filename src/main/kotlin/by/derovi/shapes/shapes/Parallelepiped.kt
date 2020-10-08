package by.derovi.shapes.shapes

import by.derovi.shapes.Name

@Name("Параллелепипед")
class Parallelepiped(
    @Name("Ширина")
    var width: Int = 0,

    @Name("Длина")
    var length: Int = 0,

    @Name("Высота")
    var height: Int = 0) : Shape() {

    override val volume: Double
        get() = width.toDouble() * length * height
}