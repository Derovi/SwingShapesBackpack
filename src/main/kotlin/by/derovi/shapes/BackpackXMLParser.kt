package by.derovi.shapes

import by.derovi.shapes.shapes.Shape
import java.io.InputStream
import java.io.OutputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult


inline fun <reified T : Shape> importBackpack(inputStream: InputStream): Backpack<T> {
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

inline fun <reified T : Shape> exportBackpack(backpack: Backpack<T>, outputStream: OutputStream) {
    val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()
    val backpackNode = document.createElement("backpack")
    backpackNode.setAttribute("size", backpack.size.toString())
    for (shape in backpack.shapes) {
        val shapeNode = document.createElement("shape")
        shapeNode.setAttribute("type", shape.javaClass.simpleName)
        for (field in shape.javaClass.declaredFields) {
            field.isAccessible = true
            val attributeNode = document.createElement("attribute")
            attributeNode.setAttribute("name", field.name)
            attributeNode.setAttribute("value", field.getInt(shape).toString())
            shapeNode.appendChild(attributeNode)
        }
        backpackNode.appendChild(shapeNode)
    }
    document.appendChild(backpackNode)
    val trans: Transformer = TransformerFactory.newInstance().newTransformer()
    trans.transform(DOMSource(document), StreamResult(outputStream))
}
