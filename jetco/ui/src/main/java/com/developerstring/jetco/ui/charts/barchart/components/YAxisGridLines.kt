package com.developerstring.jetco.ui.charts.barchart.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.developerstring.jetco.ui.charts.barchart.config.GridLineStyle

/**
 * A composable function that draws a series of horizontal grid lines at intervals along the Y-axis.
 * This is useful for visualizing grid alignment in charts, particularly for aligning data points along
 * the Y-axis.
 *
 * @param gridLineStyle Configuration object for the appearance of the grid lines including color, stroke width,
 *        dash length, and gap length.
 * @param yAxisStepHeight The height of each step in the Y-axis grid lines. Determines the vertical spacing
 *        between grid lines.
 *
 * @see GridLineStyle
 */
@Composable
fun YAxisGridLines(
    gridLineStyle: GridLineStyle,
    yAxisStepHeight: Dp
) {
    Column(
        modifier = Modifier
            .padding(top = yAxisStepHeight)
            .fillMaxWidth(),
    ) {
        repeat(gridLineStyle.totalGridLines + 1) { index ->
            if (index != 0) {
                Row(
                    modifier = Modifier.height(height = yAxisStepHeight), // Sets the height of each row of Grid Line
                    verticalAlignment = Alignment.Top
                ) {
                    GridLine(
                        modifier = Modifier
                            .fillMaxWidth(),
                        gridLineStyle = gridLineStyle,
                    )
                }
            }
        }
    }
}