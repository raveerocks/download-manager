package io.raveerocks.downloadmanager.core.views.custom

import android.view.GestureDetector
import android.view.MotionEvent


class MotionLayoutEventListener(private val onTap: () -> Unit) : GestureDetector.OnGestureListener {

    override fun onDown(event: MotionEvent): Boolean {
        return true
    }

    override fun onShowPress(e: MotionEvent) {}

    override fun onSingleTapUp(event: MotionEvent): Boolean {
        onTap()
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

    override fun onLongPress(e: MotionEvent) {
    }

    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }

}