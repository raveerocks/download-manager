package io.raveerocks.downloadmanager.core.views.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import io.raveerocks.downloadmanager.R
import io.raveerocks.downloadmanager.core.helpers.MemoryUtil.findSizeString
import io.raveerocks.downloadmanager.core.types.MediaType
import io.raveerocks.downloadmanager.core.views.chart.pie.PieChartView


class MediaDistributionPieChartView(context: Context, attrs: AttributeSet) :
    PieChartView(context, attrs) {

    private val colorAudio: Int
    private val colorImage: Int
    private val colorVideo: Int
    private val colorText: Int
    private val colorOther: Int

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.MediaDistributionView, 0, 0)
            .apply {
                try {
                    colorAudio = getColor(R.styleable.MediaDistributionView_audioColor, Color.WHITE)
                    colorImage = getColor(R.styleable.MediaDistributionView_imageColor, Color.RED)
                    colorVideo = getColor(R.styleable.MediaDistributionView_videoColor, Color.GREEN)
                    colorText = getColor(R.styleable.MediaDistributionView_textColor, Color.BLUE)
                    colorOther =
                        getColor(R.styleable.MediaDistributionView_otherColor, Color.YELLOW)
                } finally {
                    recycle()
                }
            }
    }

    fun submit(currentStatus: Map<MediaType, Float>) {
        currentStatus.let {
            val data = ChartData(
                listOf(
                    ChartItem(
                        context.getString(R.string.label_audio),
                        (currentStatus[MediaType.AUDIO] ?: 0).toFloat(),
                        colorAudio
                    ),
                    ChartItem(
                        context.getString(R.string.label_image),
                        (currentStatus[MediaType.IMAGE] ?: 0).toFloat(),
                        colorImage
                    ),
                    ChartItem(
                        context.getString(R.string.label_video),
                        (currentStatus[MediaType.VIDEO] ?: 0).toFloat(),
                        colorVideo
                    ),
                    ChartItem(
                        context.getString(R.string.label_text),
                        (currentStatus[MediaType.TEXT] ?: 0).toFloat(),
                        colorText
                    ),
                    ChartItem(
                        context.getString(R.string.label_other),
                        (currentStatus[MediaType.OTHER] ?: 0).toFloat(),
                        colorOther
                    ),
                )
            ) { value: Float -> value.findSizeString() }
            onSubmit(data)
        }
    }

}