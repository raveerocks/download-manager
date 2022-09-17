package io.raveerocks.downloadmanager.core.views.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import io.raveerocks.downloadmanager.R
import io.raveerocks.downloadmanager.core.models.ActiveDownload
import io.raveerocks.downloadmanager.core.types.ActiveDownloadStatus
import io.raveerocks.downloadmanager.core.views.chart.pie.PieChartView

class ProgressPieChartView(context: Context, attrs: AttributeSet) : PieChartView(context, attrs) {

    private val colorCompleted: Int
    private val colorPending: Int
    private val colorQueued: Int

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ProgressView, 0, 0)
            .apply {
                try {
                    colorCompleted =
                        getColor(R.styleable.ProgressView_completedSectorColor, Color.GREEN)
                    colorPending = getColor(R.styleable.ProgressView_pendingSectorColor, Color.GRAY)
                    colorQueued = getColor(R.styleable.ProgressView_queuedSectorColor, Color.RED)

                } finally {
                    recycle()
                }
            }
    }

    fun submit(progress: ActiveDownload.ActiveDownloadDetails) {
        progress.let {
            if (progress.status == ActiveDownloadStatus.IN_PROGRESS) {
                val data: ChartData<Float> = ChartData(
                    listOf(
                        ChartItem("", progress.totalBytesDownloadedSoFar, colorCompleted),
                        ChartItem(
                            "",
                            progress.totalSizeBytes - progress.totalBytesDownloadedSoFar,
                            colorPending
                        ),
                        ChartItem("", 0f, colorQueued),
                    ),
                ) { "" }
                onSubmit(data)
            }
        }
    }

}