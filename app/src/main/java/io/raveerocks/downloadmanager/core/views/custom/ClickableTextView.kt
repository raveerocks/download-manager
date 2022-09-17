package io.raveerocks.downloadmanager.core.views.custom

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.animation.doOnEnd

class ClickableTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {

    override fun performClick(): Boolean {
        val animator = ObjectAnimator.ofFloat(this, View.ALPHA, 0f)
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.repeatCount = 1
        animator.disableViewDuringAnimation(this)
        animator.start()
        animator.duration = 50
        animator.doOnEnd { super.performClick() }
        return true
    }

    private fun ObjectAnimator.disableViewDuringAnimation(view: View) {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator) {
                view.isEnabled = true
            }
        })
    }
}