package com.developerstring.jetco.ui.charts.piechart.config

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.developerstring.jetco.ui.charts.piechart.PieChart

/**
 * PieChartDefaults provides default configurations and styling options for Pie Charts.
 * It offers factory methods to create instances of [PieChartConfig] and [PieChartAnimationConfig]
 * with default or customized settings.
 */
object PieChartDefaults {

    /**
     * A default [TextStyle] used for text elements within the Pie Chart, such as item labels.
     */
    private val textStyle = TextStyle(
        fontSize = 14.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        color = Color.Black,
    )

    /**
     * A default list of colors used for the [PieChart] slices when no custom colors are provided.
     * The colors in this list are designed to be visually appealing and distinct, ensuring
     * that each slice of the pie chart is easily distinguishable.
     */
    private val defaultColorList = listOf(
        Color(0xFF519DE9),
        Color(0xFF007BFF),
        Color(0xFF03DAC5),
        Color(0xFF625CEE),
        Color(0xFFF7B7A3),
        Color(0xFFEA5F89),
        Color(0xFF9B3192),
        Color(0xFF2954A1),
    )

    /**
     * Factory method to create a [PieChartConfig] instance with default or custom settings.
     * This configuration controls the appearance of the pie chart, including its size, thickness,
     * colors, and text styling.
     *
     * @param radius The radius of the pie chart, defining its overall size.
     * @param thickness The thickness of the pie chart slices, determining how wide each slice appears.
     * @param colorsList A list of colors for the slices. If not provided, a default color list is used.
     * @param enableChartItems Boolean flag to enable or disable the display of chart items like legends or labels.
     * @param isChartItemScrollEnable Boolean flag to enable or disable scrolling of chart items when there are too many to fit within the available space.
     * @param textStyle The [TextStyle] applied to slice labels. If not provided, a default text style is used.
     * @return A configured [PieChartConfig] instance.
     */
    fun pieChartConfig(
        radius: Dp = 75.dp,
        thickness: Dp = 25.dp,
        colorsList: List<Color> = defaultColorList,
        enableChartItems: Boolean = true,
        isChartItemScrollEnable: Boolean = false,
        textStyle: TextStyle = PieChartDefaults.textStyle
    ): PieChartConfig {

        return PieChartConfig(
            radius = radius,
            thickness = thickness,
            colorsList = colorsList,
            enableChartItems = enableChartItems,
            isChartItemScrollEnable = isChartItemScrollEnable,
            textStyle = textStyle
        )

    }

    /**
     * Factory method to create a [PieChartAnimationConfig] instance with default or custom settings.
     * This configuration controls the animation behavior of the pie chart, including the animation
     * duration and the number of rotations.
     *
     * @param enableAnimation Boolean flag to enable or disable the pie chart animation.
     * @param animationDuration The duration of the animation in milliseconds.
     * @param animationRotations The number of rotations the pie chart performs during the animation.
     * @return A configured [PieChartAnimationConfig] instance.
     */
    fun pieChartAnimationConfig(
        enableAnimation: Boolean = true,
        animationDuration: Int = 1000,
        animationRotations: Int = 11,
    ): PieChartAnimationConfig {
        return PieChartAnimationConfig(
            enableAnimation = enableAnimation,
            animationRotations = animationRotations,
            animationDuration = animationDuration
        )
    }

}