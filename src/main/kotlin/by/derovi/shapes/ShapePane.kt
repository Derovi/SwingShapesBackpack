package by.derovi.shapes

import by.derovi.shapes.shapes.Shape
import java.awt.*
import javax.swing.*


class ShapePane(val shape: Shape) : JPanel(GridLayout(0, 1)) {
    val label = JLabel("Фигура")
    val deleteButton = JButton("Удалить")

    init {
        label.text = shape.javaClass.simpleName
        if (shape.javaClass.isAnnotationPresent(Name::class.java)) {
            label.text = shape.javaClass.getAnnotation(Name::class.java).name
        }
        val topLayout = JPanel(BorderLayout())
        topLayout.minimumSize = Dimension(0, 300)
        topLayout.add(label, BorderLayout.WEST)
        topLayout.add(deleteButton, BorderLayout.EAST)
        add(topLayout)
        add(JLabel("Объем: ${shape.volume}"))
        deleteButton.addActionListener {
            App.backpack.shapes.remove(shape)
            App.update()
        }
        background = Color(209, 218, 232)
        topLayout.background = Color(209, 218, 232)
        for (field in shape.javaClass.declaredFields) {
            field.isAccessible = true
            val bottomLayout = JPanel(BorderLayout())
            bottomLayout.add(JSeparator(1), BorderLayout.WEST)
            bottomLayout.add(JLabel("${
                if (field.isAnnotationPresent(Name::class.java)) {
                    field.getAnnotation(Name::class.java).name
                } else field.name}: ${field.get(shape)}"))
            add(bottomLayout)
            bottomLayout.background = Color(209, 218, 232)
            border = BorderFactory.createEmptyBorder(5, 5, 5, 5)
        }
    }
}