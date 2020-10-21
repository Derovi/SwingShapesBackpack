package by.derovi.shapes

import by.derovi.shapes.shapes.Shape
import org.xml.sax.SAXException
import java.awt.*
import javax.swing.*
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
import javax.xml.validation.Validator


object App {
    val sizeLabel = JLabel()
    val frame: JFrame
    val topPane: JPanel
    val contentPane: JPanel
    val shapesPane: JPanel
    val backpackSizeLayout: JPanel
    val changeSizeButton: JButton
    val addShapeButton: JButton

    var backpack = Backpack<Shape>(20)

    init {
        try {
            backpack = importBackpack(javaClass.classLoader.getResourceAsStream("backpack.xml"))
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

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
        val scrollPane = JScrollPane(shapesPane)
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED)
        scrollPane.maximumSize = Dimension(300, 300)
        scrollPane.minimumSize = Dimension(300, 300)
        scrollPane.preferredSize = Dimension(300, 300)

        scrollPane.setBounds(50, 30, 300, 50)
        contentPane.add(scrollPane)
        scrollPane.add(JButton("ff"))
        frame.contentPane = contentPane
        frame.size = Dimension(350, 450)
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
        changeSizeButton.addActionListener {
            val dialog = JDialog(frame)
            dialog.setLocationRelativeTo(frame)
            val panel = JPanel(GridLayout(3, 1, 10, 10))
            dialog.minimumSize = Dimension(200, 200)
            panel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
            panel.add(JLabel("Введите новый размер:"))
            val textField = JTextField(backpack.size.toString())
            panel.add(textField)
            val button = Button("ОК")
            panel.add(button)
            button.addActionListener {
                val newSize = textField.text.toIntOrNull()
                if (newSize != null && newSize > backpack.shapes.size) {
                    backpack.size = newSize
                    dialog.dispose()
                    update()
                } else {
                    JOptionPane.showMessageDialog(
                        dialog,
                        "Размер должен быть положительным числом и быть не меньше количества уже добавленных фигур",
                        "Размер указан не верно!",
                        JOptionPane.ERROR_MESSAGE
                    )
                }
            }
            dialog.contentPane = panel
            dialog.isVisible = true
        }
        addShapeButton.addActionListener {
            val dialog = JDialog(frame)
            dialog.setLocationRelativeTo(frame)
            dialog.add(AddShapePane())
            dialog.minimumSize = Dimension(80, 240)
            dialog.isVisible = true
        }
        val menuBar = JMenuBar()
        val menu = JMenu("File")
        menuBar.add(menu)
        val importItem = JMenuItem("Import")
        val exportItem = JMenuItem("Export")
        menu.add(importItem)
        menu.add(exportItem)
        frame.jMenuBar = menuBar
        importItem.addActionListener {
            val fc = JFileChooser()
            if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                val factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema")
                val schema = factory.newSchema(StreamSource(javaClass.classLoader.getResourceAsStream("backpackSchema.xsd")))
                val validator: Validator = schema.newValidator()
                try {
                    validator.validate(StreamSource(fc.selectedFile.inputStream()))
                    backpack = importBackpack(fc.selectedFile.inputStream())
                } catch (ex: SAXException) {
                    JOptionPane.showMessageDialog(
                        frame,
                        ex.message,
                        "Validation error",
                        JOptionPane.ERROR_MESSAGE
                    )
                }
            }
            update()
        }
        exportItem.addActionListener {
            val fc = JFileChooser()
            if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                exportBackpack(backpack, fc.selectedFile.outputStream())
            }
        }

        update()
    }

    fun update() {
        sizeLabel.text = "Размер: ${backpack.shapes.size} из ${backpack.size}"
        shapesPane.removeAll()
        for (shape in backpack.shapes) {
            shapesPane.add(ShapePane(shape))
        }

        for (component in contentPane.components) {
            if (component is ShapePane) {
                contentPane.remove(component)
            }
        }
        if (backpack.shapes.isEmpty()) {
            shapesPane.add(JPanel())
        }
        frame.revalidate()
    }
}
