package by.derovi.shapes.shapes

import by.derovi.shapes.Name
import kotlin.math.pow

@Name("Сфера")
class Sphere(
    @field:Name("Радиус")
    val radius: Int = 0) : Shape() {

    override val volume: Double
        get() = 4.0 / 3.0 * Math.PI * radius.toDouble().pow(3.0)
}