package by.derovi.shapes

import java.awt.Color
import java.awt.GridLayout
import java.lang.Exception
import java.util.*
import javax.swing.*
import kotlin.collections.ArrayList


class AddShapePane : JPanel(GridLayout(0, 1)) {
    init {
        build()
    }

    fun build(selected: Int = 0) {
        removeAll()
        val names = ArrayList<String>()
        for (shapeClass in shapes) {
            var name = shapeClass.java.simpleName
            if (shapeClass.java.isAnnotationPresent(Name::class.java)) {
                name = shapeClass.java.getAnnotation(Name::class.java).name
            }
            names.add(name)
        }
        val comboBox = JComboBox(names.toArray())
        comboBox.selectedIndex = selected
        comboBox.addActionListener {
            build(comboBox.selectedIndex)
        }
        add(comboBox)

        val inputs = ArrayList<JTextField>()

        for (field in shapes[selected].java.declaredFields) {
            val layout = JPanel(GridLayout(1, 0))
            field.isAccessible = true
            layout.add(JLabel("${
                if (field.isAnnotationPresent(Name::class.java)) {
                    field.getAnnotation(Name::class.java).name
                } else field.name}: "))
            val input = JTextField((Random().nextInt(10) + 1).toString())
            layout.add(input)
            layout.background = Color(209, 218, 232)
            inputs.add(input)
            add(layout)
        }

        val okButton = JButton("OK")
        val cancelButton = JButton("Cancel")
        add(okButton)
        add(cancelButton)
        val root = SwingUtilities.getRoot(this)
        if (root != null) {
            val dialog = root as JDialog
            dialog.revalidate()
        }

        okButton.addActionListener {
            val shape = shapes[selected].java.newInstance()
            val dialog = SwingUtilities.getRoot(this) as JDialog
            try {
                for ((index, field) in shape.javaClass.declaredFields.withIndex()) {
                    val value = inputs[index].text.toIntOrNull()
                    if (value == null || value < 1) {
                        throw InvalidPropertiesFormatException("Invalid input")
                    }
                    field.isAccessible = true
                    field.set(shape, value)
                }
                App.backpack.add(shape)
                App.update()
                dialog.dispose()
            } catch (exception: Backpack.BackPackOverflowException) {
                JOptionPane.showMessageDialog(dialog, exception.message, "Нет места!", JOptionPane.ERROR_MESSAGE)
            } catch (exception: Exception) {
                JOptionPane.showMessageDialog(dialog, "Параметры указаны не верно", "Ошибка", JOptionPane.ERROR_MESSAGE)
            }
        }

        cancelButton.addActionListener {
            val dialog = SwingUtilities.getRoot(this) as JDialog
            dialog.dispose()
        }
    }
}