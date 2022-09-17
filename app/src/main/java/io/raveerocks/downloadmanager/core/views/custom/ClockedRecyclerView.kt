package io.raveerocks.downloadmanager.core.views.custom

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import io.raveerocks.downloadmanager.R

class ClockedRecyclerView(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {

    open class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        protected open fun onTick() {}

        fun syncWith(parent: ClockedRecyclerView) {
            parent.lifecycleOwner?.let {
                parent.clock.observe(it) {
                    onTick()
                }
            }
        }

    }

    companion object {
        private const val DEFAULT_RESTART_INTERVAL = 60000
        private const val DEFAULT_TICK_INTERVAL = 500
    }

    val clock: LiveData<Long>
        get() = _clock
    var lifecycleOwner: LifecycleOwner? = null

    // Private variables
    private lateinit var timer: CountDownTimer
    private var _clock = MutableLiveData(0L)

    // Custom attributes of the view
    private val pRestartInterval: Long
    private val pTickInterval: Long

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ClockedRecyclerView, 0, 0)
            .apply {
                try {
                    pRestartInterval =
                        getInteger(
                            R.styleable.ClockedRecyclerView_restartInterval,
                            DEFAULT_RESTART_INTERVAL
                        ).toLong()
                    pTickInterval =
                        getColor(
                            R.styleable.ClockedRecyclerView_tickInterval,
                            DEFAULT_TICK_INTERVAL
                        ).toLong()

                } finally {
                    recycle()
                }
            }
    }

    override fun onAttachedToWindow() {
        timer = object : CountDownTimer(pRestartInterval, pTickInterval) {
            override fun onTick(millisUntilFinished: Long) {
                _clock.value = (_clock.value?.plus(1))?.rem(Long.MAX_VALUE)
            }

            override fun onFinish() {
                timer.start()
            }
        }
        timer.start()
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        timer.cancel()
        super.onDetachedFromWindow()
    }

}