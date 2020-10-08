package by.derovi.shapes

import by.derovi.shapes.shapes.Cube
import by.derovi.shapes.shapes.Parallelepiped
import by.derovi.shapes.shapes.Shape
import by.derovi.shapes.shapes.Sphere
import java.awt.FlowLayout
import java.lang.Exception
import javax.swing.*
import kotlin.reflect.KClass


class AddShapePane : JPanel(FlowLayout()) {
    companion object {
        val shapes: List<KClass<out Shape>> = listOf(Cube::class, Parallelepiped::class, Sphere::class)
    }

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
                App.backpack.add(shape)
            } catch (exception: Exception) {
                JOptionPane.showMessageDialog(dialog, "Ошибка",
                    exception.message, JOptionPane.ERROR_MESSAGE);
            }
            println(App.backpack.shapes.size)
            App.update()
            dialog.dispose()
        }

        cancelButton.addActionListener {
            val dialog = SwingUtilities.getRoot(this) as JDialog
            dialog.dispose()
        }
    }
}