package com.developerstring.jetco.ui.charts.piechart.config

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

/**
 * PieChartConfig is a data class used to configure the appearance and behavior of a Pie Chart.
 * It allows customization of various visual aspects like the radius, thickness, colors, and text style,
 * as well as enabling or disabling chart items and scroll behavior.
 *
 * @param radius Radius of the pie chart, defining its overall size.
 * @param thickness Thickness of the pie chart slices, determining how wide each slice appears.
 * @param colorsList List of colors to be used for the slices in the pie chart. The list should have enough colors
 * to cover all slices; otherwise, colors will be reused.
 * @param enableChartItems Boolean flag to enable or disable the display of chart items, such as legends or labels.
 * @param isChartItemScrollEnable Boolean flag to enable or disable scrolling of chart items when there are too many
 * items to fit within the available space.
 * @param textStyle [TextStyle] to be applied to any text elements within the pie chart, such as slice labels.
 */
@Stable
data class PieChartConfig(
    val radius: Dp,
    val thickness: Dp,
    val colorsList: List<Color>,
    val enableChartItems: Boolean,
    val isChartItemScrollEnable: Boolean,
    val textStyle: TextStyle
)

/**
 * PieChartAnimationConfig is a data class used to configure the animation behavior of a Pie Chart.
 * It allows customization of whether animations are enabled, the duration of the animation,
 * and the number of rotations the chart undergoes during the animation.
 *
 * @param enableAnimation Boolean flag to enable or disable the pie chart animation.
 * @param animationDuration Duration of the animation in milliseconds, determining how long the animation takes to complete.
 * @param animationRotations Number of rotations the pie chart performs during the animation.
 */
@Stable
data class PieChartAnimationConfig(
    val enableAnimation: Boolean,
    val animationDuration: Int,
    val animationRotations: Int,
)