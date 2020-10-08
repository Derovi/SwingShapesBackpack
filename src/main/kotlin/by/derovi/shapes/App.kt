package by.derovi.shapes

import by.derovi.shapes.shapes.Cube
import by.derovi.shapes.shapes.Parallelepiped
import by.derovi.shapes.shapes.Shape
import by.derovi.shapes.shapes.Sphere
import com.sun.jdi.JDIPermission
import java.awt.*
import javax.swing.*

object App {
    val sizeLabel = JLabel()
    val frame: JFrame
    val topPane: JPanel
    val contentPane: JPanel
    val shapesPane: JPanel
    val backpackSizeLayout: JPanel
    val changeSizeButton: JButton
    val addShapeButton: JButton
    //val bottomLayout: JPanel

    val backpack = Backpack<Shape>(20)

    init {
        frame = JFrame("App")
        contentPane = JPanel(FlowLayout(5, 10, 10))
        contentPane.border = BorderFactory.createEmptyBorder(10, 10, 0, 10)
        topPane = JPanel(GridLayout(0, 1, 10, 20))
        topPane.size = Dimension(400, 20)
        backpackSizeLayout = JPanel(BorderLayout(10, 10))
        changeSizeButton = JButton("Изменить размер")
        backpackSizeLayout.add(sizeLabel, BorderLayout.WEST)
        backpackSizeLayout.add(changeSizeButton, BorderLayout.EAST)
        topPane.add(backpackSizeLayout)
        addShapeButton = JButton("Добавить фигуру")
        topPane.add(addShapeButton)
        contentPane.add(topPane)
        contentPane.add(JSeparator())
        //contentPane.add(JSeparator())
        shapesPane = JPanel(GridLayout(0, 1, 10, 10))
        contentPane.add(shapesPane)
        frame.contentPane = contentPane
        frame.size = Dimension(200, 300)
        frame.isVisible = true
        backpack.add(Sphere(10.0))
        backpack.add(Sphere(1.0))
        backpack.add(Cube(8))
        backpack.add(Parallelepiped(8, 1, 2))
        changeSizeButton.addActionListener {
            val dialog = JDialog()
            val panel = JPanel(GridLayout(3, 1, 10, 10))
            dialog.minimumSize = Dimension(200, 200)
            panel.minimumSize = Dimension(200, 200)
            panel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
            panel.add(JLabel("Введите новый размер:"))
            val textField = JTextField(backpack.size.toString())
            panel.add(textField)
            val button = Button("ОК")
            panel.add(button)
            button.addActionListener {
                val newSize = textField.text.toIntOrNull()
                if (newSize != null && newSize > 0) {
                    backpack.size = newSize
                    dialog.dispose()
                    update()
                } else {
                    JOptionPane.showMessageDialog(dialog, "Размер должен быть положительным числом",
                        "Размер указан не верно!", JOptionPane.ERROR_MESSAGE);
                }
            }
            dialog.contentPane = panel
            dialog.isVisible = true
        }
        addShapeButton.addActionListener {
            val dialog = JDialog()
            dialog.add(AddShapePane())
            dialog.isVisible = true
        }
        update()
    }

    fun update() {
        sizeLabel.text = "Размер: ${backpack.size}"
        shapesPane.removeAll()
        for (shape in backpack.shapes) {
            shapesPane.add(ShapePane(shape))
        }

        for (component in contentPane.components) {
            if (component is ShapePane) {
                contentPane.remove(component)
            }
        }

        frame.revalidate();
    }
}