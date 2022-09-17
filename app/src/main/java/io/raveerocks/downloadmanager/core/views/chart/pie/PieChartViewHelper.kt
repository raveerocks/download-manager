package io.raveerocks.downloadmanager.core.views.chart.pie

import android.content.Context
import android.util.AttributeSet
import io.raveerocks.downloadmanager.R

class PieChartViewHelper constructor(context: Context, attrs: AttributeSet) {

    companion object {
        const val TITLE_POSITION_TOP = 0
        const val TITLE_POSITION_BOTTOM = 1

        const val LEGEND_POSITION_TOP = 0
        const val LEGEND_POSITION_RIGHT = 1
        const val LEGEND_POSITION_BOTTOM = 2
        const val LEGEND_POSITION_LEFT = 3

        private const val FACTOR_UNDEFINED = -1f
        private const val FACTOR_HIDE = 0f
        private const val FACTOR_TITLE_HEIGHT = 0.2f
        private const val FACTOR_LEGEND_X_WIDTH = 1f
        private const val FACTOR_LEGEND_X_HEIGHT = 0.1f
        private const val FACTOR_LEGEND_Y_WIDTH = 0.3f
        private const val FACTOR_LEGEND_Y_HEIGHT = 1f

        private const val PIE_INTERNAL_PADDING_FACTOR = 0.1f
        private const val TITLE_INTERNAL_PADDING_FACTOR = 0.1f
        private const val LEGEND_INTERNAL_PADDING_FACTOR = 0.1f

        private const val LEGEND_LABEL_INTERNAL_PADDING_FACTOR = 1.1f
    }

    val pieLeft: Float
        get() = _pieLeft
    val pieTop: Float
        get() = _pieTop
    val pieWidth: Float
        get() = _pieWidth
    val pieHeight: Float
        get() = _pieHeight
    val titleLeft: Float
        get() = _titleLeft
    val titleTop: Float
        get() = _titleTop
    val titleWidth: Float
        get() = _titleWidth
    val titleHeight: Float
        get() = _titleHeight
    val legendLeft: Float
        get() = _legendLeft
    val legendTop: Float
        get() = _legendTop
    val legendWidth: Float
        get() = _legendWidth
    val legendHeight: Float
        get() = _legendHeight
    val legendItemIndicatorWidth: Float
        get() = _legendItemIndicatorWidth
    val legendItemIndicatorHeight: Float
        get() = _legendItemIndicatorHeight
    val legendItemLabelHeight: Float
        get() = if (_legendItemHeightFactor == 0f) _legendItemBlockHeight - _legendItemIndicatorHeight else _legendItemIndicatorHeight

    private var _pieWidthFactor = FACTOR_UNDEFINED
    private var _pieHeightFactor = FACTOR_UNDEFINED
    private var _pieLeft = 0f
    private var _pieTop = 0f
    private var _pieWidth = 0f
    private var _pieHeight = 0f

    private val _showTitle: Boolean
    private val _titlePosition: Int
    private val _titleWeight: Float
    private var _titleWidthFactor = FACTOR_UNDEFINED
    private var _titleHeightFactor = FACTOR_UNDEFINED
    private var _titleLeft = 0f
    private var _titleTop = 0f
    private var _titleWidth = 0f
    private var _titleHeight = 0f

    private val _showLegend: Boolean
    private var _legendPosition: Int
    private val _legendWeight: Float
    private var _legendWidthFactor = FACTOR_UNDEFINED
    private var _legendHeightFactor = FACTOR_UNDEFINED
    private var _legendLeft = 0f
    private var _legendTop = 0f
    private var _legendWidth = 0f
    private var _legendHeight = 0f

    private var _legendItemCount = 0
    private var _legendItemWidthFactor = 0f
    private var _legendItemHeightFactor = 0f

    private var _legendItemBlockWidth = 0f
    private var _legendItemBlockHeight = 0f

    private var _legendItemIndicatorWidthFactor = FACTOR_UNDEFINED
    private var _legendItemIndicatorHeightFactor = FACTOR_UNDEFINED
    private var _legendItemIndicatorWidth = 0f
    private var _legendItemIndicatorHeight = 0f


    init {

        context.theme.obtainStyledAttributes(attrs, R.styleable.ChartView, 0, 0)
            .apply {
                _showTitle = getBoolean(R.styleable.ChartView_showTitle, true)
                _titlePosition =
                    getInteger(R.styleable.ChartView_titlePosition, TITLE_POSITION_BOTTOM)
                _titleWeight =
                    getFraction(R.styleable.ChartView_titleWeight, 1, 1, FACTOR_UNDEFINED)
            }

        context.theme.obtainStyledAttributes(attrs, R.styleable.PieChartView, 0, 0)
            .apply {
                _showLegend = getBoolean(R.styleable.PieChartView_showLegend, true)
                _legendPosition =
                    getInteger(R.styleable.PieChartView_legendPosition, LEGEND_POSITION_BOTTOM)
                _legendWeight =
                    getFraction(R.styleable.PieChartView_legendWeight, 1, 1, FACTOR_UNDEFINED)
            }

        if (_showLegend) {
            when (_legendPosition) {
                LEGEND_POSITION_TOP -> {
                    _legendWidthFactor = FACTOR_LEGEND_X_WIDTH
                    _legendHeightFactor =
                        if (_legendWeight == FACTOR_UNDEFINED) FACTOR_LEGEND_X_HEIGHT else _legendWeight
                }
                LEGEND_POSITION_RIGHT -> {
                    _legendWidthFactor =
                        if (_legendWeight == FACTOR_UNDEFINED) FACTOR_LEGEND_Y_WIDTH else _legendWeight
                    _legendHeightFactor = 1f
                }
                LEGEND_POSITION_BOTTOM -> {
                    _legendWidthFactor = FACTOR_LEGEND_X_WIDTH
                    _legendHeightFactor =
                        if (_legendWeight == FACTOR_UNDEFINED) FACTOR_LEGEND_X_HEIGHT else _legendWeight
                }
                LEGEND_POSITION_LEFT -> {
                    _legendWidthFactor =
                        if (_legendWeight == FACTOR_UNDEFINED) FACTOR_LEGEND_Y_WIDTH else _legendWeight
                    _legendHeightFactor = FACTOR_LEGEND_Y_HEIGHT
                }
            }
        } else {
            _legendWidthFactor = FACTOR_HIDE
            _legendHeightFactor = FACTOR_HIDE
        }

        if (_showTitle) {
            if (_legendPosition == LEGEND_POSITION_LEFT || _legendPosition == LEGEND_POSITION_RIGHT) {
                _titleWidthFactor = 1f - _legendWidthFactor
                _titleHeightFactor =
                    if (_titleWeight == FACTOR_UNDEFINED) FACTOR_TITLE_HEIGHT else _titleWeight
            } else {
                _titleWidthFactor = 1f
                _titleHeightFactor =
                    if (_titleWeight == FACTOR_UNDEFINED) FACTOR_TITLE_HEIGHT else _titleWeight
            }
        } else {
            _titleWidthFactor = FACTOR_HIDE
            _titleHeightFactor = FACTOR_HIDE
        }

        if (_legendPosition == LEGEND_POSITION_TOP || _legendPosition == LEGEND_POSITION_BOTTOM) {
            _pieHeightFactor = 1f - (_legendHeightFactor + _titleHeightFactor)
            _pieWidthFactor = 1f
            _legendItemIndicatorWidthFactor = 1f
            _legendItemIndicatorHeightFactor = 0.5f
        } else {
            _pieHeightFactor = 1f - _titleHeightFactor
            _pieWidthFactor = 1f - _legendWidthFactor
            _legendItemIndicatorWidthFactor = 0.5f
            _legendItemIndicatorHeightFactor = 1f
        }
    }

    fun recalculatePositions(paddingLeft: Float, paddingTop: Float, width: Float, height: Float) {

        val pieWidth = width * _pieWidthFactor
        val pieHeight = height * _pieHeightFactor
        val titleWidth = width * _titleWidthFactor
        val titleHeight = height * _titleHeightFactor
        val legendWidth = width * _legendWidthFactor
        val legendHeight = height * _legendHeightFactor

        var pieLeft = 0f
        var pieTop = 0f
        var titleLeft = 0f
        var titleTop = 0f
        var legendLeft = 0f
        var legendTop = 0f


        when (_legendPosition) {
            LEGEND_POSITION_TOP -> {
                when (_titlePosition) {
                    TITLE_POSITION_TOP -> {
                        titleLeft = paddingLeft
                        legendLeft = titleLeft
                        pieLeft = legendLeft
                        titleTop = paddingTop
                        legendTop = titleTop + titleHeight
                        pieTop = legendTop + legendHeight
                    }
                    TITLE_POSITION_BOTTOM -> {
                        legendLeft = paddingLeft
                        pieLeft = legendLeft
                        titleLeft = pieLeft
                        legendTop = paddingTop
                        pieTop = legendTop + legendHeight
                        titleTop = pieTop + pieHeight
                    }
                }
            }
            LEGEND_POSITION_RIGHT -> {
                when (_titlePosition) {
                    TITLE_POSITION_TOP -> {
                        titleLeft = paddingLeft
                        legendLeft = titleLeft + titleWidth
                        pieLeft = titleLeft
                        titleTop = paddingTop
                        legendTop = titleTop
                        pieTop = titleTop + titleHeight
                    }
                    TITLE_POSITION_BOTTOM -> {
                        pieLeft = paddingLeft
                        legendLeft = pieLeft + pieWidth
                        titleLeft = pieLeft
                        pieTop = paddingTop
                        legendTop = pieTop
                        titleTop = pieTop + pieHeight
                    }
                }
            }
            LEGEND_POSITION_BOTTOM -> {
                when (_titlePosition) {
                    TITLE_POSITION_TOP -> {
                        titleLeft = paddingLeft
                        pieLeft = legendLeft
                        legendLeft = titleLeft
                        titleTop = paddingTop
                        pieTop = titleTop + titleHeight
                        legendTop = pieTop + pieHeight
                    }
                    TITLE_POSITION_BOTTOM -> {
                        pieLeft = paddingLeft
                        titleLeft = pieLeft
                        legendLeft = titleLeft
                        pieTop = paddingTop
                        titleTop = pieTop + pieHeight
                        legendTop = titleTop + titleHeight
                    }
                }
            }
            LEGEND_POSITION_LEFT -> {
                when (_titlePosition) {
                    TITLE_POSITION_TOP -> {
                        legendLeft = paddingLeft
                        titleLeft = legendLeft + legendWidth
                        pieLeft = titleLeft
                        legendTop = paddingTop
                        titleTop = legendTop
                        pieTop = titleTop + titleHeight
                    }
                    TITLE_POSITION_BOTTOM -> {
                        legendLeft = paddingLeft
                        pieLeft = legendLeft + legendWidth
                        titleLeft = pieLeft
                        legendTop = paddingTop
                        pieTop = legendTop
                        titleTop = pieTop + pieHeight
                    }
                }
            }
        }

        val min = pieWidth.coerceAtMost(pieHeight)
        val minPadding = min * PIE_INTERNAL_PADDING_FACTOR
        val pieLeftPadding = (pieWidth - min).coerceAtLeast(0f) / 2 + minPadding
        val pieTopPadding = (pieHeight - min).coerceAtLeast(0f) / 2 + minPadding
        val titlePadding = titleWidth.coerceAtMost(titleHeight) * TITLE_INTERNAL_PADDING_FACTOR
        val legendPadding = legendWidth.coerceAtMost(legendHeight) * LEGEND_INTERNAL_PADDING_FACTOR


        this._pieLeft = pieLeft + pieLeftPadding
        this._pieTop = pieTop + pieTopPadding
        this._pieWidth = pieWidth - 2 * pieLeftPadding
        this._pieHeight = pieHeight - 2 * pieTopPadding

        this._titleLeft = titleLeft + titlePadding
        this._titleTop = titleTop + titlePadding
        this._titleWidth = titleWidth - 2 * titlePadding
        this._titleHeight = titleHeight - 2 * titlePadding

        this._legendLeft = legendLeft + legendPadding
        this._legendTop = legendTop + legendPadding
        this._legendWidth = legendWidth - 2 * legendPadding
        this._legendHeight = legendHeight - 2 * legendPadding

        resetLegendPosition()
    }

    fun resetLegendItemCount(count: Int) {
        _legendItemCount = count
        resetLegendPosition()
    }

    fun getLegendItemIndicatorLeft(index: Int): Float {
        return index * _legendItemWidthFactor
    }

    fun getLegendItemIndicatorTop(index: Int): Float {
        return index * _legendItemHeightFactor
    }

    fun getLegendItemLabelLeft(index: Int): Float {
        if (_legendItemWidthFactor == 0f) return LEGEND_LABEL_INTERNAL_PADDING_FACTOR * _legendItemIndicatorWidth
        return getLegendItemIndicatorLeft(index)
    }

    fun getLegendItemLabelTop(index: Int): Float {
        if (_legendItemHeightFactor == 0f) return _legendItemIndicatorHeight
        return getLegendItemIndicatorTop(index)
    }

    private fun resetLegendPosition() {
        val legendBlockWidth: Float
        val legendBlockHeight: Float
        if (_legendPosition == LEGEND_POSITION_TOP || _legendPosition == LEGEND_POSITION_BOTTOM) {
            legendBlockWidth = legendWidth / _legendItemCount
            legendBlockHeight = legendHeight
            _legendItemWidthFactor = legendBlockWidth
            _legendItemHeightFactor = 0f
        } else {
            legendBlockWidth = legendWidth
            legendBlockHeight = legendHeight / _legendItemCount
            _legendItemWidthFactor = 0f
            _legendItemHeightFactor = legendBlockHeight
        }
        _legendItemBlockWidth = legendBlockWidth
        _legendItemBlockHeight = legendBlockHeight

        _legendItemIndicatorWidth = _legendItemBlockWidth * _legendItemIndicatorWidthFactor
        _legendItemIndicatorHeight = _legendItemBlockHeight * _legendItemIndicatorHeightFactor
    }

}