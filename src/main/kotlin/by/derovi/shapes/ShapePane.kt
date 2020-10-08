package by.derovi.shapes

import by.derovi.shapes.shapes.Shape
import java.awt.BorderLayout
import java.awt.Color
import java.awt.GridLayout
import javax.swing.*

class ShapePane(val shape: Shape) : JPanel(GridLayout(0, 1, 5, 5)) {
    val label = JLabel("Фигура")
    val deleteButton = JButton("Удалить")

    init {
        label.text = shape.javaClass.simpleName
        if (shape.javaClass.isAnnotationPresent(Name::class.java)) {
            label.text = shape.javaClass.getAnnotation(Name::class.java).name
        }
        val topLayout = JPanel(BorderLayout())
        topLayout.add(label, BorderLayout.WEST)
        topLayout.add(deleteButton, BorderLayout.EAST)
        add(topLayout)
        add(JLabel("Объем: ${shape.volume}"))
        deleteButton.addActionListener {
            App.backpack.shapes.remove(shape)
            App.update()
        }
        val bottomLayout = JPanel(BorderLayout())
        bottomLayout.add(JSeparator(1), BorderLayout.WEST)
        add(bottomLayout)
        background = Color(209, 218, 232)
        topLayout.background = Color(209, 218, 232)
        bottomLayout.background = Color(209, 218, 232)
        border = BorderFactory.createEmptyBorder(5, 5, 5, 5)

    }
}