package com.developerstring.jetco.ui.charts.barchart.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

/**
 * A composable function that displays a label for the X-axis of a chart. The label can be rotated based on
 * the provided configuration and can trigger a click action.
 *
 * @param itemName The text to be displayed as the X-axis label.
 * @param xAxisTextStyle The [TextStyle] to be applied to the X-axis label, including font size, weight, color, and more.
 * @param textRotateAngle The angle (in degrees) by which the label text should be rotated. Applied only if rotation
 *        is enabled.
 * @param enableTextRotate A boolean flag indicating whether the label text should be rotated.
 * @param onLabelClick A lambda function to be executed when the X-axis label is clicked.
 */
@Composable
fun XAxisLabel(
    itemName: String,
    xAxisTextStyle: TextStyle,
    textRotateAngle: Float,
    enableTextRotate: Boolean,
    onLabelClick: () -> Unit
) {

    // Composable function to display the X-axis label with click functionality.
    val xAxisScaleTextUI: @Composable () -> Unit = {
        Text(
            text = itemName,
            style = xAxisTextStyle, // Applies the specified text style to the label
            modifier = Modifier.clickable {
                onLabelClick()
            }
        )
    }

    if (enableTextRotate) {
        // Box used to contain the label text with rotation applied if rotation is enabled
        Box(
            modifier = Modifier
                .size(itemName.length.dp * (xAxisTextStyle.fontSize.value / 2f))
                .rotate(textRotateAngle)
        ) {
            xAxisScaleTextUI()
        }
    } else {
        xAxisScaleTextUI() // Displays the label text without rotation
    }

}