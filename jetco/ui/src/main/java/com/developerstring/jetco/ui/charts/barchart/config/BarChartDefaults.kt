package com.developerstring.jetco.ui.charts.barchart.config

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Default configurations and utilities for the BarChart components.
 */
object BarChartDefaults {

    /**
     * Default [TextStyle] used for axis labels in the bar chart.
     */
    private val textStyle = TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        color = Color.Black,
    )

    /**
     * Default enter transition for animating the bars in the column bar chart.
     *
     * This transition expands the bars vertically when they
     * first appear on the screen. It provides a smooth and visually appealing
     * entrance for the bars in the chart.
     *
     * @see EnterTransition
     */
    val enterTransitionVertically = expandVertically(animationSpec = tween(durationMillis = 1000))

    /**
     * Provides default configuration for bars in a bar chart.
     *
     * @param color The color of the bar.
     * @param height The height of the bar.
     * @param width The width of the bar.
     * @param shape The shape of the bar (e.g., rounded corners).
     * @return A [BarChartConfig] with default values.
     */
    fun columnBarChartConfig(
        color: Color = Color(0xFFEFB8C8),
        height: Dp = 200.dp,
        width: Dp = 20.dp,
        shape: Shape = RoundedCornerShape(
            8.dp
        )
    ) = BarChartConfig(
        color = color,
        height = height,
        width = width,
        shape = shape
    )

    /**
     * Provides default configuration for grouped bars in a bar chart.
     *
     * @param colors A list of colors for the bars in each group.
     * @param height The height of the bars.
     * @param width The width of the bars.
     * @param shape The shape of the bars (e.g., rounded corners).
     * @param gapBetweenBar The gap between individual bars within a group.
     * @param gapBetweenGroup The gap between different groups of bars.
     * @return A [GroupBarChartConfig] with default values.
     */
    fun groupBarChartConfig(
        colors: List<Color> = listOf(
            Color(0xFF0066cc),
            Color(0xFF8BC1F7),
            Color(0xFF519DE9),
            Color(0xFFB2B0EA)
        ),
        height: Dp = 200.dp,
        width: Dp = 20.dp,
        shape: Shape = RoundedCornerShape(
            6.dp
        ),
        gapBetweenBar: Dp = 0.dp,
        gapBetweenGroup: Dp = 20.dp
    ) = GroupBarChartConfig(
        colors = colors,
        height = height,
        width = width,
        shape = shape,
        gapBetweenBar = gapBetweenBar,
        gapBetweenGroup = gapBetweenGroup
    )

    /**
     * Provides default configuration for the X-axis in a bar chart.
     *
     * @param isAxisScaleEnabled Indicates whether the axis scale is enabled.
     * @param isAxisLineEnabled Indicates whether the axis line is enabled.
     * @param axisLineWidth The width of the axis line.
     * @param axisLineShape The shape of the axis line (e.g., rounded corners).
     * @param axisLineColor The color of the axis line.
     * @param textStyle The style of the text for axis labels.
     * @return An [XAxisConfig] with default values.
     */
    fun xAxisConfig(
        isAxisScaleEnabled: Boolean = true,
        isAxisLineEnabled: Boolean = true,
        axisLineWidth: Dp = 2.dp,
        axisLineShape: Shape = RoundedCornerShape(3.dp),
        axisLineColor: Color = Color.LightGray,
        textStyle: TextStyle = BarChartDefaults.textStyle,
    ) = XAxisConfig(
        isAxisScaleEnabled = isAxisScaleEnabled,
        isAxisLineEnabled = isAxisLineEnabled,
        axisLineWidth = axisLineWidth,
        axisLineShape = axisLineShape,
        axisLineColor = axisLineColor,
        textStyle = textStyle,
    )

    /**
     * Provides default configuration for the Y-axis in a bar chart.
     *
     * @param isAxisScaleEnabled Indicates whether the axis scale is enabled.
     * @param isAxisLineEnabled Indicates whether the axis line is enabled.
     * @param axisLineWidth The width of the axis line.
     * @param axisLineShape The shape of the axis line (e.g., rounded corners).
     * @param axisLineColor The color of the axis line.
     * @param axisScaleCount The number of scale divisions on the Y-axis.
     * @param textStyle The style of the text for axis labels.
     * @param textPrefix The prefix to be added before the scale value.
     * @param textPostfix The postfix to be added after the scale value.
     * @return A [YAxisConfig] with default values.
     */
    fun yAxisConfig(
        isAxisScaleEnabled: Boolean = true,
        isAxisLineEnabled: Boolean = true,
        axisLineWidth: Dp = 2.dp,
        axisLineShape: Shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp),
        axisLineColor: Color = Color.LightGray,
        axisScaleCount: Int = 4,
        textStyle: TextStyle = BarChartDefaults.textStyle,
        textPrefix: String = "",
        textPostfix: String = ""
    ) = YAxisConfig(
        isAxisScaleEnabled = isAxisScaleEnabled,
        isAxisLineEnabled = isAxisLineEnabled,
        axisLineWidth = axisLineWidth,
        axisLineShape = axisLineShape,
        axisLineColor = axisLineColor,
        axisScaleCount = axisScaleCount,
        textStyle = textStyle,
        textPrefix = textPrefix,
        textPostfix = textPostfix
    )

    /**
     * Provides default style for grid lines in the bar chart.
     *
     * @param color The color of the grid lines.
     * @param strokeWidth The width of the grid lines.
     * @param dashLength The length of each dash in dashed grid lines.
     * @param gapLength The gap between dashes in dashed grid lines.
     * @param totalGridLines The total number of grid lines to be drawn.
     * @param dashCap The style of the stroke cap used for dashed lines.
     * @return A [GridLineStyle] with default values.
     */
    fun gridLineStyle(
        color: Color = Color.LightGray,
        strokeWidth: Dp = 1.dp,
        dashLength: Dp = 8.dp,
        gapLength: Dp = 8.dp,
        totalGridLines: Int = 4,
        dashCap: StrokeCap = StrokeCap.Square
    ) = GridLineStyle(
        color = color,
        strokeWidth = strokeWidth,
        dashLength = dashLength,
        gapLength = gapLength,
        totalGridLines = totalGridLines,
        dashCap = dashCap
    )

    /**
     * Provides default configuration for pop-ups in the bar chart.
     *
     * @param enableXAxisPopUp Indicates whether pop-ups are enabled for the X-axis labels.
     * @param enableBarPopUp Indicates whether pop-ups are enabled for the bars.
     * @param background The background color of the pop-ups.
     * @param shape The shape of the pop-ups (e.g., rounded corners).
     * @param textStyle The style of the text in the pop-ups.
     * @return A [PopUpConfig] with default values.
     */
    fun popUpConfig(
        enableXAxisPopUp: Boolean = true,
        enableBarPopUp: Boolean = true,
        background: Color = Color(0xFFCCC2DC),
        shape: Shape = RoundedCornerShape(25),
        textStyle: TextStyle = BarChartDefaults.textStyle
    ) = PopUpConfig(
        enableXAxisPopUp = enableXAxisPopUp,
        enableBarPopUp = enableBarPopUp,
        background = background,
        shape = shape,
        textStyle = textStyle
    )
}
