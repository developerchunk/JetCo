package com.developerstring.jetco.ui.charts.barchart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.developerstring.jetco.ui.charts.barchart.config.YAxisConfig
import java.text.DecimalFormat
import kotlin.math.abs

/**
 * A composable function that displays the Y-axis scale labels and an optional Y-axis line for a chart.
 *
 * @param yAxisConfig Configuration for the Y-axis, including text style, scale count, line appearance, and more.
 * @param yAxisStepHeight The height of each step in the Y-axis scale.
 * @param yAxisScaleStep The step value used to calculate the scale labels on the Y-axis.
 * @param barHeight The height of the Y-axis line.
 *
 * @see YAxisConfig
 */
@Composable
fun YAxisScale(
    yAxisConfig: YAxisConfig,
    yAxisStepHeight: Dp,
    yAxisScaleStep: Float,
    barHeight: Dp,
) {
    Row {
        // Column for the Y-axis scale labels
        Column(horizontalAlignment = Alignment.End) {
            repeat(yAxisConfig.axisScaleCount + 1) { index ->
                val barScale = (yAxisConfig.axisScaleCount) - index
                Row(
                    modifier = Modifier.height(height = yAxisStepHeight),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = yAxisConfig.textPrefix + formatScaleValue(yAxisScaleStep * barScale) + yAxisConfig.textPostfix,
                        style = yAxisConfig.textStyle
                    )
                }
            }
        }


        // Y-axis line
        if (yAxisConfig.isAxisLineEnabled) {
            Spacer(modifier = Modifier.width(10.dp)) // Adds spacing between scale labels and Y-axis line

            Box(
                modifier = Modifier
                    .padding(top = yAxisStepHeight)
                    .clip(shape = yAxisConfig.axisLineShape)
                    .width(yAxisConfig.axisLineWidth)
                    .height(barHeight)
                    .background(yAxisConfig.axisLineColor)
            )
        }
    }
}

/**
 * Formats a numeric value for display on the Y-axis scale, using abbreviated notation for large values.
 *
 * This function converts large numeric values into a more readable format with suffixes:
 * - Values below 10,000 are displayed as-is.
 * - Values in the millions are formatted with an "M" suffix.
 * - Values in the thousands are formatted with a "K" suffix.
 *
 * @param value The numeric value to be formatted.
 * @return A formatted string representing the value with appropriate suffixes for readability.
 */
fun formatScaleValue(value: Float): String {

    // Defines the decimal format to display up to two decimal places
    val df = DecimalFormat("##.##")

    // For values less than 10,000, display the value as is
    if (value < 10000) {
        return df.format(value)
    }

    val am: Float
    // For values in millions, format the value with an "M" suffix
    if (abs(value / 1000000) >= 1) {
        am = value / 1000000
        return df.format(am) + "M"
    } else if (abs(value / 1000) >= 1) { // For values in thousands, format the value with a "K" suffix
        am = value / 1000
        return df.format(am) + "K"
    } else {
        // Default case, return the formatted value without suffix
        return df.format(value)
    }

}