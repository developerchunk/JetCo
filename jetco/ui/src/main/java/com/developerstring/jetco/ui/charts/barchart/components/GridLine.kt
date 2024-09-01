package com.developerstring.jetco.ui.charts.barchart.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import com.developerstring.jetco.ui.charts.barchart.config.GridLineStyle

/**
 * A composable function that draws a grid line in a chart. This is typically used to represent grid lines
 * on the background of a chart to aid in visualizing data values.
 *
 * @param modifier Modifier to be applied to the grid line.
 * @param gridLineStyle Style configuration for the grid line, including color, stroke width, dash length, and gap length.
 *
 * @see GridLineStyle
 */
@Composable
fun GridLine(
    modifier: Modifier = Modifier,
    gridLineStyle: GridLineStyle,
) {
    Canvas(modifier = modifier.padding(top = gridLineStyle.strokeWidth/2)) {
        // Convert Dp to pixels
        val dashLengthPx = gridLineStyle.dashLength.toPx()
        val gapLengthPx = gridLineStyle.gapLength.toPx()
        val strokeWidthPx = gridLineStyle.strokeWidth.toPx()

        // Define the PathEffect to create dashes
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashLengthPx, gapLengthPx), 0f)

        // Draw the dashed line
        drawLine(
            color = gridLineStyle.color,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = strokeWidthPx,
            pathEffect = pathEffect,
            cap = gridLineStyle.dashCap
        )
    }
}
