package io.raveerocks.downloadmanager.core.views.chart.pie

import android.view.GestureDetector
import android.view.MotionEvent

class LegendSectionEventListener(
    private val left: Float,
    private val top: Float,
    private val width: Float,
    private val height: Float,
    private val onTap: (Float, Float) -> Unit
) : GestureDetector.OnGestureListener {

    fun isWithin(x: Float, y: Float): Boolean {
        return x >= left && x <= left + width && y >= top && y <= top + height
    }

    override fun onDown(e: MotionEvent): Boolean {
        return true
    }

    override fun onShowPress(e: MotionEvent) {}

    override fun onSingleTapUp(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        onTap(x, y)
        return true
    }

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent) {}

    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }


}