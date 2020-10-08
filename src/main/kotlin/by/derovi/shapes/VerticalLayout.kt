package by.derovi.shapes

import java.awt.Component
import java.awt.Container
import java.awt.Dimension
import java.awt.LayoutManager

internal class VerticalLayout : LayoutManager {
    private val size = Dimension()
    override fun addLayoutComponent(name: String, comp: Component) {}
    override fun removeLayoutComponent(comp: Component) {}
    override fun minimumLayoutSize(c: Container): Dimension {
        return calculateBestSize(c)
    }

    override fun preferredLayoutSize(c: Container): Dimension {
        return calculateBestSize(c)
    }

    override fun layoutContainer(container: Container) {
        val list = container.components
        var currentY = 5
        for (i in list.indices) {
            val pref = list[i].preferredSize
            list[i].setBounds(5, currentY, pref.width, pref.height)
            currentY += 5
            currentY += pref.height
        }
    }

    private fun calculateBestSize(c: Container): Dimension {
        val list = c.components
        var maxWidth = 0
        for (i in list.indices) {
            val width = list[i].width
            if (width > maxWidth) maxWidth = width
        }
        size.width = maxWidth + 5
        var height = 0
        for (i in list.indices) {
            height += 5
            height += list[i].height
        }
        size.height = height
        return size
    }
}