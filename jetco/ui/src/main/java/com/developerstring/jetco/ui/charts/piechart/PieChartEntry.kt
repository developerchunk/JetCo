package com.developerstring.jetco.ui.charts.piechart

import androidx.compose.ui.graphics.Color

/**
 * Data class representing a single entry in a Pie Chart.
 *
 * @param name The label or name of the pie chart slice.
 * @param value The value associated with this slice. This value determines the size of the slice in the pie chart.
 * @param color The color used to represent this slice in the pie chart.
 */
data class PieChartEntry(
    val name: String,
    val value: Float,
    val color: Color
)

/**
 * Extension function that maps a [Map] of chart data to a list of [PieChartEntry] objects.
 * Each entry in the map is transformed into a [PieChartEntry] with a corresponding color.
 *
 * @param colors A list of colors to be used for the pie chart slices. The colors are assigned
 *               to the entries in order. If there are more entries than colors, the colors
 *               will be reused with a reduced alpha value.
 *
 * @return A list of [PieChartEntry] objects, each representing a slice in the pie chart with
 *         a name, value, and color.
 */
fun Map<String, Float>.mapToPieChartEntries(
    colors: List<Color>
): List<PieChartEntry> {

    return this.entries.mapIndexed { index, entry ->
        PieChartEntry(
            name = entry.key,
            value = entry.value,
            color = colors.getOrElse(index) { colors[index % colors.size].copy(alpha = 0.5f) }
        )
    }
}