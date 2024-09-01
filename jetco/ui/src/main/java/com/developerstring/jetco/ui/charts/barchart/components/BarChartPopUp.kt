package com.developerstring.jetco.ui.charts.barchart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.developerstring.jetco.ui.charts.barchart.config.PopUpConfig

/**
 * A composable function that displays a popup with text information related to a bar in the bar chart.
 * The popup can be used to show details or additional information when interacting with a bar chart.
 *
 * @param popUpConfig Configuration for the popup's appearance and behavior. This includes settings for
 *                    the popup's shape, background color, text style, and whether it is enabled.
 * @param text The text to be displayed inside the popup.
 * @param onDismissRequest A lambda function that is invoked when the popup needs to be dismissed.
 *                          This allows for handling the action to close or hide the popup.
 *
 * @see PopUpConfig
 */
@Composable
fun BarChartPopup(
    popUpConfig: PopUpConfig,
    text: String,
    onDismissRequest: () -> Unit
) {
    // Popup content configuration
    Popup(
        alignment = Alignment.Center,
        onDismissRequest = { onDismissRequest() },
    ) {
        // Content of the popup
        Box(
            modifier = Modifier
                .background(
                    color = popUpConfig.background,
                    shape = popUpConfig.shape
                )
                .padding(8.dp)
        ) {
            Text(
                text = text,
                style = popUpConfig.textStyle
            )
        }
    }

}