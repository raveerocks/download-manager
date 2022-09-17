package io.raveerocks.downloadmanager.core.views.chart

import android.content.Context
import android.util.AttributeSet
import android.view.View

abstract class ChartView<T>(context: Context, attrs: AttributeSet) : View(context, attrs) {

    data class ChartItem<T>(val label: String, val value: T, val color: Int)
    data class ChartData<T>(val items: List<ChartItem<T>>, val format: (T) -> String)

    abstract fun onSubmit(data: ChartData<T>)
}