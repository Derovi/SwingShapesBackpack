package by.derovi.shapes.shapes

import by.derovi.shapes.Name
import kotlin.math.pow

@Name("Сфера")
class Sphere(
    @Name("Радиус")
    val radius: Double = 0.0) : Shape() {

    override val volume: Double
        get() = 4.0 / 3.0 * Math.PI * radius.pow(3.0)
}