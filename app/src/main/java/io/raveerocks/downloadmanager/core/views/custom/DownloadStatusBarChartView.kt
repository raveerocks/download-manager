package io.raveerocks.downloadmanager.core.views.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import io.raveerocks.downloadmanager.R
import io.raveerocks.downloadmanager.core.types.DownloadStatus
import io.raveerocks.downloadmanager.core.views.chart.bar.BarChartView

class DownloadStatusBarChartView(context: Context, attrs: AttributeSet) :
    BarChartView(context, attrs) {

    private val colorActive: Int
    private val colorDone: Int
    private val colorFailed: Int

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.DownloadStatusView, 0, 0)
            .apply {
                try {
                    colorActive =
                        getColor(R.styleable.DownloadStatusView_activeSectorColor, Color.GREEN)
                    colorDone = getColor(R.styleable.DownloadStatusView_doneSectorColor, Color.GRAY)
                    colorFailed =
                        getColor(R.styleable.DownloadStatusView_failedSectorColor, Color.RED)

                } finally {
                    recycle()
                }
            }
    }

    fun submit(currentStatus: Map<DownloadStatus, Int>) {
        currentStatus.let {
            val data = ChartData(
                listOf(
                    ChartItem(
                        context.getString(R.string.label_active),
                        (currentStatus[DownloadStatus.ACTIVE] ?: 0).toFloat(),
                        colorActive
                    ),
                    ChartItem(
                        context.getString(R.string.label_done),
                        (currentStatus[DownloadStatus.DONE] ?: 0).toFloat(),
                        colorDone
                    ),
                    ChartItem(
                        context.getString(R.string.label_failed),
                        (currentStatus[DownloadStatus.FAILED] ?: 0).toFloat(),
                        colorFailed
                    ),
                ),
            ) { value: Float -> value.findFileString() }
            onSubmit(data)
        }
    }

    private fun Float.findFileString(): String {
        val value = this.toInt()
        return if (value != 1) {
            "$value ${context.getString(R.string.label_file)}"
        } else {
            "$value ${context.getString(R.string.label_files)}"
        }
    }

}