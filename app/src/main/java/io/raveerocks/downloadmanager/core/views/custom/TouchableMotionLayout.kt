package io.raveerocks.downloadmanager.core.views.custom

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.constraintlayout.motion.widget.MotionLayout

class TouchableMotionLayout constructor(context: Context, attrs: AttributeSet) :
    MotionLayout(context, attrs) {

    private lateinit var clickEventListener: MotionLayoutEventListener
    private lateinit var clickGestureDetector: GestureDetector

    fun setOnTap(onTap: () -> Unit) {
        clickEventListener = MotionLayoutEventListener(onTap)
        clickGestureDetector = GestureDetector(context, clickEventListener)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val handled = clickGestureDetector.onTouchEvent(event)
        return handled || super.onTouchEvent(event)
    }
}