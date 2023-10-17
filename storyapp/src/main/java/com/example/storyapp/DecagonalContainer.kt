package com.example.storyapp

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class DecagonalContainer(context: Context, attrs: AttributeSet? = null) : ViewGroup(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Measure the children and calculate the size of the container
        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
        }
        val width = resolveSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = resolveSize(suggestedMinimumHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // Position the children within the decagon
        val childCount = childCount
        val centerX = (r - l) / 2
        val centerY = (b - t) / 2
        val radius = Math.min(centerX, centerY) * 0.8

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val angle = (i * 36).toDouble() * Math.PI / 180.0
            val childX = (centerX + radius * Math.cos(angle)).toInt()
            val childY = (centerY + radius * Math.sin(angle)).toInt()

            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight
            val childLeft = childX - childWidth / 2
            val childTop = childY - childHeight / 2
            val childRight = childLeft + childWidth
            val childBottom = childTop + childHeight

            child.layout(childLeft, childTop, childRight, childBottom)
        }
    }
}
