package io.raveerocks.downloadmanager.core.views.chart.pie

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.withTranslation
import io.raveerocks.downloadmanager.R
import io.raveerocks.downloadmanager.core.views.chart.ChartView
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


open class PieChartView(context: Context, attrs: AttributeSet) : ChartView<Float>(context, attrs) {

    private data class PieChartItem(
        val label: String,
        val value: Float,
        val color: Int,
        val startAngle: Float,
        val sweepAngle: Float
    )

    companion object {
        private const val DEFAULT_PIE_START = 0.0f
        private const val PIE_HIGHLIGHT_UNKNOWN = -1
        private const val DEFAULT_TITLE_COLOR = Color.BLACK
        private const val SIZE_FIT_TO_LAYOUT = -1f
        private const val DEFAULT_LEGEND_LABEL_COLOR = Color.BLACK
        private const val DEFAULT_PIE_SECTOR_LABEL_COLOR = Color.BLACK
        private val DEFAULT_TEXT_FONT_FACE = Typeface.SERIF
    }

    // Custom attributes of the view
    private val pInteractive: Boolean
    private var pPieStart: Float
    private var pPieSectorLabelSize: Float
    private val pTitle: String
    private val pTitleSize: Float
    private val pLegendItemLabelSize: Float

    // Data to be displayed
    private var items: List<PieChartItem> = ArrayList()

    // PieChartViewHelper instance attached to the current measures
    private var pieChartViewHelper: PieChartViewHelper

    // EventListeners instance attached to the View
    private lateinit var pieSectionEventListener: PieSectionEventListener
    private lateinit var legendSectionEventListener: LegendSectionEventListener

    // GestureDetectors instance attached to the View
    private lateinit var pieGestureDetector: GestureDetector
    private lateinit var legendGestureDetector: GestureDetector

    // Helper objects for drawing Pie
    private val pieRect = RectF(0f, 0f, 0f, 0f)
    private val piePaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val highLightedPiePaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        setShadowLayer(10f, 0f, 0f, Color.parseColor("#000001"))
    }
    private val pieSectorLabelTextPaint = Paint(ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
    }
    private lateinit var displayFormat: (Float) -> String
    private var pieHighlightedSector = PIE_HIGHLIGHT_UNKNOWN

    // Helper objects for drawing Title
    private val titleTextPaint = Paint(ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = DEFAULT_TEXT_FONT_FACE
    }

    // Helper objects for drawing Legend
    private val legendItemIndicatorRect = RectF(0f, 0f, 0f, 0f)
    private val legendItemLabelTextPaint = Paint(ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.LEFT
        typeface = DEFAULT_TEXT_FONT_FACE
    }

    init {
        pieChartViewHelper = PieChartViewHelper(context, attrs)
        context.theme.obtainStyledAttributes(attrs, R.styleable.ChartView, 0, 0)
            .apply {
                try {
                    pInteractive = getBoolean(R.styleable.ChartView_interactive, true)
                    pTitle = getString(R.styleable.ChartView_title)
                        ?: context.getString(R.string.default_pie_chart_title)
                    titleTextPaint.apply {
                        color = getColor(R.styleable.ChartView_titleColor, DEFAULT_TITLE_COLOR)
                    }
                    pTitleSize = getDimension(
                        R.styleable.ChartView_titleSize,
                        SIZE_FIT_TO_LAYOUT
                    )
                    getResourceId(R.styleable.ChartView_fontFamily, -1).let {
                        if (it != -1) {
                            pieSectorLabelTextPaint.apply {
                                typeface = ResourcesCompat.getFont(context, it)
                            }
                            legendItemLabelTextPaint.apply {
                                typeface = ResourcesCompat.getFont(context, it)
                            }
                        }
                    }

                } finally {
                    recycle()
                }
            }
        context.theme.obtainStyledAttributes(attrs, R.styleable.PieChartView, 0, 0)
            .apply {
                try {
                    pPieStart = getFloat(R.styleable.PieChartView_pieStart, DEFAULT_PIE_START)
                    pPieSectorLabelSize = getDimension(
                        R.styleable.PieChartView_pieSectorLabelSize,
                        SIZE_FIT_TO_LAYOUT
                    )
                    pieSectorLabelTextPaint.apply {
                        color = getColor(
                            R.styleable.PieChartView_pieSectorLabelColor,
                            DEFAULT_PIE_SECTOR_LABEL_COLOR
                        )
                    }
                    legendItemLabelTextPaint.apply {
                        color = getColor(
                            R.styleable.PieChartView_legendLabelColor,
                            DEFAULT_LEGEND_LABEL_COLOR
                        )

                    }
                    pLegendItemLabelSize = getDimension(
                        R.styleable.PieChartView_legendLabelSize,
                        SIZE_FIT_TO_LAYOUT
                    )
                } finally {
                    recycle()
                }
            }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var width = 0
        var height = 0

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            width = widthSize
            height = heightSize
        } else if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.AT_MOST) {
            width = widthSize
            height = min(heightSize, widthSize)
        } else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.EXACTLY) {
            width = min(heightSize, widthSize)
            height = heightSize
        } else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            width = min(heightSize, widthSize)
            height = width
        } else if (widthMode == MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.UNSPECIFIED) {
            width = 200
            height = 200
        } else {
            if (widthMode == MeasureSpec.UNSPECIFIED) {
                height = heightSize
                width = height
            } else if (heightMode == MeasureSpec.UNSPECIFIED) {
                width = widthSize
                height = width
            }
        }

        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val xPadding = (paddingLeft + paddingRight).toFloat()
        val yPadding = (paddingTop + paddingBottom).toFloat()
        val width = w.toFloat() - xPadding
        val height = h.toFloat() - yPadding
        pieChartViewHelper.recalculatePositions(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            width,
            height
        )
        onPieSizeChanged()
        onTitleSizeChanged()
        onLegendSizeChanged()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.apply {
            withTranslation(pieChartViewHelper.pieLeft, pieChartViewHelper.pieTop) { drawPie() }
            withTranslation(
                pieChartViewHelper.titleLeft,
                pieChartViewHelper.titleTop
            ) { drawTitle() }
            withTranslation(
                pieChartViewHelper.legendLeft,
                pieChartViewHelper.legendTop
            ) { drawLegend() }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (pInteractive) {
            val x = event?.x!!
            val y = event.y
            if (pieSectionEventListener.isWithin(x, y)) {
                pieGestureDetector.onTouchEvent(event)
                return true
            } else if (legendSectionEventListener.isWithin(x, y)) {
                legendGestureDetector.onTouchEvent(event)
                return true
            }
        }
        performClick()
        return false
    }

    override fun onSubmit(data: ChartData<Float>) {
        var total: Float
        data.items
            .map { it.value }
            .reduce { acc, value -> acc + value }
            .also { total = it }

        var startAngle = 0f

        items = data.items.map { item ->
            val sweep = item.value * 360 / total
            val currentStartAngle = startAngle
            startAngle += sweep
            return@map PieChartItem(
                item.label,
                item.value,
                item.color,
                currentStartAngle,
                sweep
            )
        }
        displayFormat = data.format
        pieChartViewHelper.resetLegendItemCount(items.size)
        invalidate()
    }

    fun getPieStart(): Float {
        return pPieStart
    }

    fun setPieStart(value: Float) {
        pPieStart = value
        invalidate()
    }

    private fun onPieSizeChanged() {
        pieRect.set(0f, 0f, pieChartViewHelper.pieWidth, pieChartViewHelper.pieHeight)
        pieSectionEventListener = PieSectionEventListener(
            pieChartViewHelper.pieLeft + pieChartViewHelper.pieWidth / 2,
            pieChartViewHelper.pieTop + pieChartViewHelper.pieHeight / 2,
            pieChartViewHelper.pieWidth / 2
        ) { angle -> onPieTap(angle) }
        pieGestureDetector = GestureDetector(context, pieSectionEventListener)
    }

    private fun onTitleSizeChanged() {
        titleTextPaint.apply {
            textSize =
                if (pTitleSize == SIZE_FIT_TO_LAYOUT) pieChartViewHelper.titleHeight else pTitleSize
        }
    }

    private fun onLegendSizeChanged() {
        legendSectionEventListener = LegendSectionEventListener(
            pieChartViewHelper.legendLeft,
            pieChartViewHelper.legendTop,
            pieChartViewHelper.legendWidth,
            pieChartViewHelper.legendHeight
        ) { x, y -> onLegendTap(x, y) }
        legendGestureDetector = GestureDetector(context, legendSectionEventListener)
    }

    private fun Canvas.drawPie() {
        items.let {
            items.forEach {
                if (it.sweepAngle != 0f) {
                    piePaint.color = it.color
                    drawArc(pieRect, pPieStart + it.startAngle, it.sweepAngle, true, piePaint)
                }
            }
            if (pieHighlightedSector != -1 && items[pieHighlightedSector].sweepAngle != 0f) {
                highLightedPiePaint.color = items[pieHighlightedSector].color
                drawArc(
                    pieRect,
                    pPieStart + items[pieHighlightedSector].startAngle,
                    items[pieHighlightedSector].sweepAngle,
                    true,
                    highLightedPiePaint
                )
                val angle =
                    pPieStart + items[pieHighlightedSector].startAngle + items[pieHighlightedSector].sweepAngle / 2
                val radius = pieChartViewHelper.pieWidth / 3
                val x = radius * cos(angle * PI / 180).toFloat() + pieChartViewHelper.pieWidth / 2
                val y = radius * sin(angle * PI / 180).toFloat() + pieChartViewHelper.pieHeight / 2
                pieSectorLabelTextPaint.apply {
                    textSize =
                        if (pPieSectorLabelSize == SIZE_FIT_TO_LAYOUT) radius / 6 else pPieSectorLabelSize
                }
                drawText(
                    displayFormat(items[pieHighlightedSector].value), x, y,
                    pieSectorLabelTextPaint
                )
            }
        }
    }

    private fun Canvas.drawTitle() {
        drawText(
            pTitle,
            pieChartViewHelper.titleWidth / 2f,
            pieChartViewHelper.titleHeight,
            titleTextPaint
        )
    }

    private fun Canvas.drawLegend() {
        items.let {

            legendItemIndicatorRect.set(
                0f,
                0f,
                pieChartViewHelper.legendItemIndicatorWidth,
                pieChartViewHelper.legendItemIndicatorHeight
            )

            for (i in items.indices) {
                withTranslation(
                    pieChartViewHelper.getLegendItemIndicatorLeft(i),
                    pieChartViewHelper.getLegendItemIndicatorTop(i)
                ) { drawLegendItemIndicator(i) }
                withTranslation(
                    pieChartViewHelper.getLegendItemLabelLeft(i),
                    pieChartViewHelper.getLegendItemLabelTop(i)
                ) { drawLegendItemLabel(i) }
            }

            if (pieHighlightedSector != PIE_HIGHLIGHT_UNKNOWN) {
                withTranslation(
                    pieChartViewHelper.getLegendItemIndicatorLeft(pieHighlightedSector),
                    pieChartViewHelper.getLegendItemIndicatorTop(pieHighlightedSector)
                ) { drawLegendItemHighlightedIndicator() }
            }
        }
    }

    private fun Canvas.drawLegendItemIndicator(i: Int) {
        piePaint.color = items[i].color
        drawRect(legendItemIndicatorRect, piePaint)
    }

    private fun Canvas.drawLegendItemLabel(i: Int) {
        legendItemLabelTextPaint.apply {
            textSize =
                if (pLegendItemLabelSize == SIZE_FIT_TO_LAYOUT) pieChartViewHelper.legendItemLabelHeight / 2 else pLegendItemLabelSize
        }
        drawText(
            items[i].label,
            0f,
            pieChartViewHelper.legendItemLabelHeight,
            legendItemLabelTextPaint
        )
    }

    private fun Canvas.drawLegendItemHighlightedIndicator() {
        highLightedPiePaint.color = items[pieHighlightedSector].color
        drawRect(legendItemIndicatorRect, highLightedPiePaint)
    }

    private fun onPieTap(tappedAngle: Float) {
        var angle = tappedAngle - pPieStart
        angle = if (angle >= 0) angle else angle + 360
        val index = items.findItemIndexByAngle(angle)
        pieHighlightedSector = if (pieHighlightedSector == index) -1 else index
        invalidate()
    }

    private fun onLegendTap(x: Float, y: Float) {
        items.let {
            for (index in items.indices) {
                val left =
                    pieChartViewHelper.legendLeft + pieChartViewHelper.getLegendItemIndicatorLeft(
                        index
                    )
                val top =
                    pieChartViewHelper.legendTop + pieChartViewHelper.getLegendItemIndicatorTop(
                        index
                    )
                val width = pieChartViewHelper.legendItemIndicatorWidth
                val height = pieChartViewHelper.legendItemIndicatorHeight
                if (x >= left && x <= left + width && y >= top && y <= top + height) {
                    pieHighlightedSector = if (pieHighlightedSector == index) -1 else index
                    invalidate()
                }
            }
        }
    }

    private fun List<PieChartItem>.findItemIndexByAngle(angle: Float): Int {
        var min = 0
        var max = size - 1
        var mid: Int
        while (min <= max) {
            mid = (min + max) / 2
            if (this[mid].startAngle + this[mid].sweepAngle < angle) {
                min = mid + 1
            } else if (this[mid].startAngle > angle) {
                max = mid - 1
            } else {
                return mid
            }
        }
        return -1
    }

}