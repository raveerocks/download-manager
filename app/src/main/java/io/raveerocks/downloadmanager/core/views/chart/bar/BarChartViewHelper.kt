package io.raveerocks.downloadmanager.core.views.chart.bar

import android.content.Context
import android.util.AttributeSet
import io.raveerocks.downloadmanager.R


class BarChartViewHelper constructor(context: Context, attrs: AttributeSet) {

    companion object {

        const val TITLE_POSITION_TOP = 0
        const val TITLE_POSITION_BOTTOM = 1

        private const val FACTOR_UNDEFINED = -1f
        private const val FACTOR_HIDE = 0f
        private const val FACTOR_TITLE_HEIGHT = 0.2f

        private const val TITLE_INTERNAL_PADDING_FACTOR = 0.1f
        private const val BAR_INTERNAL_PADDING_FACTOR = 0.0f
    }

    val titleLeft: Float
        get() = _titleLeft
    val titleTop: Float
        get() = _titleTop
    val titleWidth: Float
        get() = _titleWidth
    val titleHeight: Float
        get() = _titleHeight
    val barLeft: Float
        get() = _barLeft
    val barTop: Float
        get() = _barTop
    val barWidth: Float
        get() = _barWidth
    val barHeight: Float
        get() = _barHeight
    /*  val barItemWidth: Float
          get() = _barItemWidth
      val barItemHeight: Float
          get() = _barItemHeight*/

    private val _showTitle: Boolean
    private val _titlePosition: Int
    private val _titleWeight: Float
    private var _titleWidthFactor = FACTOR_UNDEFINED
    private var _titleHeightFactor = FACTOR_UNDEFINED
    private var _titleLeft = 0f
    private var _titleTop = 0f
    private var _titleWidth = 0f
    private var _titleHeight = 0f

    private var _barWidthFactor = FACTOR_UNDEFINED
    private var _barHeightFactor = FACTOR_UNDEFINED
    private var _barLeft = 0f
    private var _barTop = 0f
    private var _barWidth = 0f
    private var _barHeight = 0f

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ChartView, 0, 0)
            .apply {
                _showTitle = getBoolean(R.styleable.ChartView_showTitle, true)
                _titlePosition =
                    getInteger(R.styleable.ChartView_titlePosition, TITLE_POSITION_BOTTOM)
                _titleWeight =
                    getFraction(R.styleable.ChartView_titleWeight, 1, 1, FACTOR_UNDEFINED)
            }

        context.theme.obtainStyledAttributes(attrs, R.styleable.BarChartView, 0, 0)
            .apply {

            }

        if (_showTitle) {
            _titleWidthFactor = 1f
            _titleHeightFactor =
                if (_titleWeight == FACTOR_UNDEFINED) FACTOR_TITLE_HEIGHT else _titleWeight

        } else {
            _titleWidthFactor = FACTOR_HIDE
            _titleHeightFactor = FACTOR_HIDE
        }

        _barWidthFactor = 1f
        _barHeightFactor = 1 - _titleHeightFactor
    }

    fun recalculatePositions(paddingLeft: Float, paddingTop: Float, width: Float, height: Float) {

        val titleWidth = width * _titleWidthFactor
        val titleHeight = height * _titleHeightFactor
        val barWidth = width * _barWidthFactor
        val barHeight = height * _barHeightFactor

        var titleLeft = 0f
        var titleTop = 0f
        var barLeft = 0f
        var barTop = 0f

        when (_titlePosition) {
            TITLE_POSITION_TOP -> {
                titleLeft = paddingLeft
                barLeft = titleLeft
                titleTop = paddingTop
                barTop = titleTop + titleHeight
            }
            TITLE_POSITION_BOTTOM -> {
                barLeft = paddingLeft
                titleLeft = barLeft
                barTop = paddingTop
                titleTop = barTop + barHeight
            }
        }

        val titlePadding = titleWidth.coerceAtMost(titleHeight) * TITLE_INTERNAL_PADDING_FACTOR
        val barPadding = barWidth.coerceAtMost(barHeight) * BAR_INTERNAL_PADDING_FACTOR

        this._titleLeft = titleLeft + titlePadding
        this._titleTop = titleTop + titlePadding
        this._titleWidth = titleWidth - 2 * titlePadding
        this._titleHeight = titleHeight - 2 * titlePadding

        this._barLeft = barLeft + barPadding
        this._barTop = barTop + barPadding
        this._barWidth = barWidth - 2 * barPadding
        this._barHeight = barHeight - 2 * barPadding

    }

}