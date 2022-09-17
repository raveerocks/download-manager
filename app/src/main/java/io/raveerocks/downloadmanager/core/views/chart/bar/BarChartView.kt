package io.raveerocks.downloadmanager.core.views.chart.bar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.RectF
import android.util.AttributeSet
import androidx.core.graphics.withTranslation
import io.raveerocks.downloadmanager.R
import io.raveerocks.downloadmanager.core.views.chart.ChartView


open class BarChartView(context: Context, attrs: AttributeSet) : ChartView<Float>(context, attrs) {

    private data class BarChartItem(
        val label: String,
        val value: Float,
        val color: Int,
        val startFactor: Float,
        val lengthFactor: Float
    )

    companion object {
        private const val DEFAULT_TITLE_COLOR = Color.BLACK
        private const val SIZE_FIT_TO_LAYOUT = -1f
        const val BAR_ORIENTATION_HORIZONTAL = 0
        const val BAR_ORIENTATION_VERTICAL = 1
    }

    // Custom attributes of the view
    private val pTitle: String
    private val pTitleSize: Float
    private val pBarItemLabelSize: Float
    private val pBarOrientation: Int

    // Data to be displayed
    private var items: List<BarChartItem> = ArrayList()

    // BarChartViewHelper instance attached to the current measures
    private var barChartViewHelper: BarChartViewHelper


    // Helper objects for drawing Title
    private val titleTextPaint = Paint(ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
    }

    // Helper objects for drawing Bar Sections
    private val barItemRect = RectF(0f, 0f, 0f, 0f)
    private val barItemPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private lateinit var displayFormat: (Float) -> String

    init {

        barChartViewHelper = BarChartViewHelper(context, attrs)
        context.theme.obtainStyledAttributes(attrs, R.styleable.ChartView, 0, 0)
            .apply {
                try {
                    pTitle = getString(R.styleable.ChartView_title)
                        ?: context.getString(R.string.default_bar_chart_title)
                    titleTextPaint.apply {
                        color = getColor(R.styleable.ChartView_titleColor, DEFAULT_TITLE_COLOR)
                    }
                    pTitleSize = getDimension(R.styleable.ChartView_titleSize, SIZE_FIT_TO_LAYOUT)
                } finally {
                    recycle()
                }
            }

        context.theme.obtainStyledAttributes(attrs, R.styleable.BarChartView, 0, 0)
            .apply {
                try {
                    pBarItemLabelSize =
                        getDimension(R.styleable.BarChartView_barItemLabelSize, SIZE_FIT_TO_LAYOUT)
                    pBarOrientation = getInteger(
                        R.styleable.BarChartView_barOrientation,
                        BAR_ORIENTATION_HORIZONTAL
                    )
                } finally {
                    recycle()
                }
            }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val xPadding = (paddingLeft + paddingRight).toFloat()
        val yPadding = (paddingTop + paddingBottom).toFloat()
        val width = w.toFloat() - xPadding
        val height = h.toFloat() - yPadding
        barChartViewHelper.recalculatePositions(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            width,
            height
        )
        onTitleSizeChanged()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.apply {
            withTranslation(
                barChartViewHelper.titleLeft,
                barChartViewHelper.titleTop
            ) { drawTitle() }
            withTranslation(barChartViewHelper.barLeft, barChartViewHelper.barTop) { drawBar() }
        }
    }

    override fun onSubmit(data: ChartData<Float>) {
        var total: Float
        data.items
            .map { it.value }
            .reduce { acc, value -> acc + value }
            .also { total = it }
        var startFactor = 0f
        items = data.items.map { item ->
            val lengthFactor = item.value / total
            val currentStartFactor = startFactor
            startFactor += lengthFactor
            return@map BarChartItem(
                item.label,
                item.value,
                item.color, currentStartFactor, lengthFactor
            )
        }
        this.displayFormat = data.format
        invalidate()
    }

    private fun onTitleSizeChanged() {
        titleTextPaint.apply {
            textSize =
                if (pTitleSize == SIZE_FIT_TO_LAYOUT) barChartViewHelper.titleHeight else pTitleSize
        }
    }

    private fun Canvas.drawTitle() {
        drawText(
            pTitle,
            barChartViewHelper.titleWidth / 2f,
            barChartViewHelper.titleHeight,
            titleTextPaint
        )
    }

    private fun Canvas.drawBar() {
        items.let {
            for (i in items.indices) {
                when (pBarOrientation) {
                    BAR_ORIENTATION_HORIZONTAL -> barItemRect.set(
                        barChartViewHelper.barWidth * items[i].startFactor,
                        0f,
                        barChartViewHelper.barWidth * (items[i].startFactor + items[i].lengthFactor),
                        barChartViewHelper.barHeight
                    )
                    BAR_ORIENTATION_VERTICAL -> barItemRect.set(
                        0f,
                        barChartViewHelper.barHeight * items[i].startFactor,
                        barChartViewHelper.barWidth,
                        barChartViewHelper.barHeight * (items[i].startFactor + items[i].lengthFactor)
                    )
                }
                drawBarItem(i)
            }

        }
    }

    private fun Canvas.drawBarItem(i: Int) {
        barItemPaint.color = items[i].color
        drawRect(barItemRect, barItemPaint)
    }

}



