package com.developerstring.jetco.ui.charts.barchart.model

import androidx.compose.runtime.Stable

/**
 * Data class representing a group of bars in a grouped bar chart.
 *
 * @property name The label associated with the group of bars, typically displayed on the X-axis.
 * @property barValues A list of [BarValue] objects representing the individual bars within this group.
 *                     Each [BarValue] contains the raw value and its normalized float value.
 */
@Stable
data class GroupBarChartItem(
    val name: String,
    val barValues: List<BarValue>
)


/**
 * Data class representing an individual bar within a group in a grouped bar chart.
 *
 * @property value The raw float value of the bar, representing the value of the bar.
 * @property floatValue The normalized value of the bar, expressed as a float between 0 and 1.
 *                      This value is computed as a fraction of the maximum value among all bars
 *                      in the group, facilitating proportional rendering of the bars.
 */
data class BarValue(
    val value: Float,
    val floatValue: Float
)

/**
 * Extension function to convert a [Map] of grouped bar data into a list of [GroupBarChartItem] objects.
 *
 * This function processes a map where each key represents a group label and the value is a list of integers,
 * each representing the height (or value) of a bar within that group. The function normalizes these heights
 * based on the provided maximum value, allowing for proportional rendering of the bars in each group.
 * The resulting list contains `GroupBarChartItem` instances, each with a name and a list of normalized bar values.
 *
 * @param maxValue The maximum value among all bars, used to compute the normalized `floatValue` for each bar.
 *                 This value determines the scale factor for normalizing the bar heights.
 * @return A list of [GroupBarChartItem] objects, where each item includes the group label and a list of
 *         `BarValue` objects representing the bars within the group. Each `BarValue` contains the bar's
 *         original value and its normalized float value.
 */
fun Map<String, List<Float>>.mapToGroupBarChartItems(maxValue: Float): List<GroupBarChartItem> =
    mapValues { (key, value) ->
        GroupBarChartItem(
            name = key,
            barValues = value.map {
                BarValue(
                    value = it,
                    floatValue = (it / maxValue)
                )
            }
        )
    }.values.toList()

