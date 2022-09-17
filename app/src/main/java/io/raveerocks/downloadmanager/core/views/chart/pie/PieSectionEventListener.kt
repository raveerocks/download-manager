package io.raveerocks.downloadmanager.core.views.chart.pie

import android.view.GestureDetector
import android.view.MotionEvent
import kotlin.math.PI
import kotlin.math.atan2

class PieSectionEventListener(
    private val centreX: Float,
    private val centreY: Float,
    private val radius: Float,
    private val onTap: (Float) -> Unit
) : GestureDetector.OnGestureListener {

    companion object {
        private const val FACTOR_RADIAN_TO_DEGREE = 180f / PI
    }

    fun isWithin(x: Float, y: Float): Boolean {
        val xDistance = getAbscissa(x)
        val yDistance = getOrdinate(y)
        return radius * radius >= xDistance * xDistance + yDistance * yDistance
    }

    override fun onDown(event: MotionEvent): Boolean {
        return true
    }

    override fun onShowPress(e: MotionEvent) {}

    override fun onSingleTapUp(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        onTap(getInclination(x, y))
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


    private fun getInclination(x: Float, y: Float): Float {
        return (atan2(getOrdinate(y), getAbscissa(x)) * FACTOR_RADIAN_TO_DEGREE).toFloat()
    }

    private fun getAbscissa(x: Float): Float {
        return x - centreX
    }

    private fun getOrdinate(y: Float): Float {
        return y - centreY
    }

}