package com.developerstring.jetco.ui.charts.barchart.config

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

/**
 * Configuration for individual bars in a bar chart.
 *
 * @property color The color of the bar.
 * @property height The height of the bar.
 * @property width The width of the bar.
 * @property shape The shape of the bar (e.g., rounded corners).
 */
@Stable
data class BarChartConfig(
    val color: Color,
    val height: Dp,
    val width: Dp,
    val shape: Shape
)

/**
 * Configuration for grouped bars in a bar chart.
 *
 * @property colors A list of colors for the bars in each group.
 * @property height The height of the bars.
 * @property width The width of the bars.
 * @property shape The shape of the bars (e.g., rounded corners).
 * @property gapBetweenBar The gap between individual bars within a group.
 * @property gapBetweenGroup The gap between different groups of bars.
 */
@Stable
data class GroupBarChartConfig(
    val colors: List<Color>,
    val height: Dp,
    val width: Dp,
    val shape: Shape,
    val gapBetweenBar: Dp,
    val gapBetweenGroup: Dp
)

/**
* Configuration for the X-axis in a bar chart.
*
* @property isAxisScaleEnabled Indicates whether the axis scale is enabled.
* @property isAxisLineEnabled Indicates whether the axis line is enabled.
* @property axisLineWidth The width of the axis line.
* @property axisLineShape The shape of the axis line (e.g., rounded corners).
* @property axisLineColor The color of the axis line.
* @property textStyle The [TextStyle] of the text for axis labels.
*/
@Stable
data class XAxisConfig(
    val isAxisScaleEnabled: Boolean,
    val isAxisLineEnabled: Boolean,
    val axisLineWidth: Dp,
    val axisLineShape: Shape,
    val axisLineColor: Color,
    val textStyle: TextStyle,
)

/**
 * Configuration for the Y-axis in a bar chart.
 *
 * @property isAxisScaleEnabled Indicates whether the axis scale is enabled.
 * @property isAxisLineEnabled Indicates whether the axis line is enabled.
 * @property axisLineWidth The width of the axis line.
 * @property axisLineShape The shape of the axis line (e.g., rounded corners).
 * @property axisLineColor The color of the axis line.
 * @property axisScaleCount The number of scale divisions on the Y-axis.
 * @property textStyle The [TextStyle] of the text for axis labels.
 * @property textPrefix The prefix to be added before the scale value.
 * @property textPostfix The postfix to be added after the scale value.
 */
@Stable
data class YAxisConfig(
    val isAxisScaleEnabled: Boolean,
    val isAxisLineEnabled: Boolean,
    val axisLineWidth: Dp,
    val axisLineShape: Shape,
    val axisLineColor: Color,
    val axisScaleCount: Int,
    val textStyle: TextStyle,
    val textPrefix: String,
    val textPostfix: String
)

/**
 * Configuration for grid lines in a bar chart.
 *
 * @property color The color of the grid lines.
 * @property strokeWidth The width of the grid lines.
 * @property dashLength The length of each dash in dashed grid lines.
 * @property gapLength The gap between dashes in dashed grid lines.
 * @property totalGridLines The total number of grid lines to be drawn in the chart.
 * @property dashCap The style of the stroke cap used for dashed lines.
 *
 */
@Stable
data class GridLineStyle(
    val color: Color,
    val strokeWidth: Dp,
    val dashLength: Dp,
    val gapLength: Dp,
    val totalGridLines: Int,
    val dashCap: StrokeCap
)

/**
 * Configuration for pop-ups in a bar chart.
 *
 * @property enableXAxisPopUp Indicates whether pop-ups are enabled for the X-axis labels.
 * @property enableBarPopUp Indicates whether pop-ups are enabled for the bars.
 * @property background The background color of the pop-ups.
 * @property shape The shape of the pop-ups (e.g., rounded corners).
 * @property textStyle The [TextStyle] of the text in the pop-ups.
 */
@Stable
data class PopUpConfig(
    val enableXAxisPopUp: Boolean,
    val enableBarPopUp: Boolean,
    val background: Color,
    val shape: Shape,
    val textStyle: TextStyle
)