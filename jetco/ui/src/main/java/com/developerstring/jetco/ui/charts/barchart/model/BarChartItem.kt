package com.developerstring.jetco.ui.charts.barchart.model

import androidx.compose.runtime.Stable

/**
 * Data class representing an individual bar in the column bar chart.
 *
 * @property name The label associated with the bar, typically displayed on the X-axis.
 * @property value The raw float value of the bar, representing the value of the bar.
 * @property floatValue The normalized value of the bar, expressed as a float between 0 and 1.
 *                      This value is computed as a fraction of the maximum value among all bars,
 *                      facilitating proportional rendering of the bars in the chart.
 */
@Stable
data class BarChartItem(
    val name: String,
    val value: Float,
    val floatValue: Float
)

/**
 * Extension function to transform a [Map] of bar data into a list of [BarChartItem] objects.
 *
 * This function processes a map where each key-value pair represents a bar's label and its corresponding
 * height (or value). It normalizes the heights based on the provided maximum value, allowing for
 * proportional rendering of bars in the chart. The resulting list contains `BarChartItem` instances
 * which include both the original bar data and its normalized form.
 *
 * @param maxValue The maximum value among all bars, used to compute the normalized `floatValue` for each bar.
 *                 This value determines the scale factor for normalizing the bar heights.
 * @return A list of [BarChartItem] objects, where each item includes the bar's name, original value,
 *         and normalized float value.
 */
fun Map<String, Float>.mapToBarChartItems(maxValue: Float): List<BarChartItem> =
    mapValues { (key, value) ->
        BarChartItem(
            name = key,
            value = value,
            floatValue = (value / maxValue)
        )
    }.values.toList()
