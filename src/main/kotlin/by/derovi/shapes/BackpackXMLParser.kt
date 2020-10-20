package by.derovi.shapes

import by.derovi.shapes.shapes.Shape
import java.io.File
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory

inline fun<reified T : Shape> parseBackpack(inputStream: InputStream): Backpack<T> {
    val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream)
    val backpack = Backpack<T>(document.childNodes.item(0).attributes.getNamedItem("size").nodeValue.toInt())
    val nodes = document.getElementsByTagName("shape")
    for (idx in 0 until nodes.length) {
        val element = nodes.item(idx)
        val shapeClass = shapes.find {
            it.simpleName == element.attributes.getNamedItem("type").nodeValue } ?: continue
        val shape = shapeClass.java.newInstance()
        val attributes = element.childNodes
        for (attributeIdx in 0 until attributes.length) {
            val attribute = attributes.item(attributeIdx)
            if (attribute.nodeName != "attribute") continue
            val attributeName = attribute.attributes.getNamedItem("name").nodeValue ?: continue
            val attributeValue = attribute.attributes.getNamedItem("value").nodeValue?.toInt() ?: continue
            val field = shapeClass.java.declaredFields.find { it.name == attributeName } ?: continue
            field.isAccessible = true
            field.set(shape, attributeValue)
        }
        if (shape is T) {
            backpack.add(shape)
        }
    }
    return backpack
}