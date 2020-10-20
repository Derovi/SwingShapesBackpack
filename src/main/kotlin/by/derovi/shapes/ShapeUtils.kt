package by.derovi.shapes

import by.derovi.shapes.shapes.Cube
import by.derovi.shapes.shapes.Parallelepiped
import by.derovi.shapes.shapes.Shape
import by.derovi.shapes.shapes.Sphere
import kotlin.reflect.KClass

val shapes: List<KClass<out Shape>> = listOf(Cube::class, Parallelepiped::class, Sphere::class)