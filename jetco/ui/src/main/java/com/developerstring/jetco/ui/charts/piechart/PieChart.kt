package com.developerstring.jetco.ui.charts.piechart

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.developerstring.jetco.ui.charts.piechart.config.PieChartAnimationConfig
import com.developerstring.jetco.ui.charts.piechart.config.PieChartConfig
import com.developerstring.jetco.ui.charts.piechart.config.PieChartDefaults
import java.text.DecimalFormat

/**
 * [PieChart] is a composable function that draws a pie chart based on the provided data and configurations.
 * The pie chart supports animation, custom colors, and displays additional chart items.
 *
 * @param modifier Modifier to be applied to the outer container of the pie chart.
 * @param chartData Map of chart entries where the key is the label and the value is the corresponding data point.
 * @param pieChartConfig Configuration for the appearance of the pie chart, such as radius, thickness, colors, and text style.
 * @param pieChartAnimationConfig Configuration for the animation of the pie chart, such as duration and rotation settings.
 * @param chartItemModifier Modifier to be applied to the chart items container, which displays additional information like labels.
 * @param chartItems Composable function that returns a list of [PieChartEntry], displaying the text, value, and color of each item.
 * @param onItemClick Lambda function that is triggered when a pie chart entry is clicked, passing the clicked [PieChartEntry] as a parameter.
 *
 * Example usage:
 * ```
 * val chartData = mapOf("Category A" to 30f, "Category B" to 20f, "Category C" to 50f)
 * PieChart(
 *     modifier = Modifier.fillMaxSize(),
 *     chartData = chartData,
 *     onItemClick = { entry -> Log.d("PieChart", "Clicked on ${entry.name}") }
 * )
 * ```
 */
@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    chartData: Map<String, Float>,
    pieChartConfig: PieChartConfig = PieChartDefaults.pieChartConfig(),
    pieChartAnimationConfig: PieChartAnimationConfig = PieChartDefaults.pieChartAnimationConfig(),
    chartItemModifier: Modifier = Modifier,
    /** This compose function returns list of [PieChartEntry] - [text, value, and the color] of each item*/
    chartItems: @Composable ((List<PieChartEntry>) -> Unit)? = null,
    onItemClick: ((PieChartEntry) -> Unit)? = null
) {

    val totalSum = chartData.values.sum()
    val floatValue = mutableListOf<Float>()

    /* To set the value of each Arc according to
    the value given in the data, we have used a simple formula.
    Calculate the sweep angle for each slice of the pie chart based on its proportion
    relative to the total sum of all values. The `floatValue` list stores these angles
    in degrees (0-360), where each angle represents the portion of the pie chart
    corresponding to the value at the given index in the `data` map. */
    chartData.values.forEachIndexed { index, values ->
        floatValue.add(index, 360 * values / totalSum)
    }

    var animationPlayed by rememberSaveable { mutableStateOf(!pieChartAnimationConfig.enableAnimation) }

    var lastValue = 0f

    // Calculating the animation size based on the radius of the pie chart
    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) pieChartConfig.radius.value * 2f else 0f,
        animationSpec = tween(
            durationMillis = pieChartAnimationConfig.animationDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ), label = "animateFloatAsState"
    )

    // Calculating the rotation of the pie chart during animation
    // 90f is used to complete 1/4 of the rotation
    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90f * pieChartAnimationConfig.animationRotations else 0f,
        animationSpec = tween(
            durationMillis = pieChartAnimationConfig.animationDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ), label = "animateFloatAsState"
    )

    val list: List<PieChartEntry> = chartData.mapToPieChartEntries(
        colors = pieChartConfig.colorsList
    )

    /* The vertical padding is calculated using a formula to ensure that the pie chart's arcs
    do not get cut off when rotated. As the pie chart is drawn using multiple arcs, rotation
    may cause parts of the arcs to extend beyond the designated area, leading to visual clipping
    at the edges (top, bottom, right, and left). This formula adjusts the padding dynamically
    based on the radius of the chart to provide enough space around the pie chart, preventing
    any part of the arcs from being cut off during rotation. */
    val pieChartPadding = (pieChartConfig.radius * 2) / (pieChartConfig.radius / 8.dp)

    val scrollState = rememberScrollState()

    // to play the animation only once when the function is Created or Recomposed
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Column(
        modifier = modifier
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Drawing the Pie Chart using Canvas Arc
        Box(

            modifier = Modifier
                .padding(all = pieChartPadding)
                .size(animateSize.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .offset { IntOffset.Zero }
                    .size(pieChartConfig.radius * 2f)
                    .rotate(animateRotation)
            ) {
                // Draw each Arc for each data entry in the Pie Chart
                floatValue.forEachIndexed { index, value ->
                    drawArc(
                        color = list[index].color,
                        lastValue,
                        value,
                        useCenter = false,
                        style = Stroke(pieChartConfig.thickness.toPx(), cap = StrokeCap.Butt),
                    )
                    lastValue += value
                }
            }
        }

        // Displaying additional chart items like labels, if enabled
        if (pieChartConfig.enableChartItems) {
            if (chartItems !== null) {
                chartItems(list)
            } else {
                PieChartItems(
                    modifier = chartItemModifier.padding(top = pieChartPadding),
                    data = list,
                    userScrollEnable = pieChartConfig.isChartItemScrollEnable,
                    scrollState = scrollState,
                    textStyle = pieChartConfig.textStyle,
                    onItemClick = onItemClick
                )
            }
        }

    }

}

/**
 * PieChartItems is a composable function that displays a list of chart items, such as labels,
 * for the PieChart. Each item shows the name, value, and color of the corresponding pie slice.
 *
 * @param modifier Modifier to be applied to the container of the chart items.
 * @param data List of [PieChartEntry] representing the pie chart slices.
 * @param userScrollEnable Boolean flag to enable or disable horizontal scrolling of chart items.
 * @param scrollState ScrollState to manage the scroll position of the chart items.
 * @param textStyle [TextStyle] applied to the text elements in the chart items.
 * @param onItemClick Lambda function that is triggered when a chart item is clicked, passing the clicked [PieChartEntry] as a parameter.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PieChartItems(
    modifier: Modifier,
    data: List<PieChartEntry>,
    userScrollEnable: Boolean,
    scrollState: ScrollState,
    textStyle: TextStyle,
    onItemClick: ((PieChartEntry) -> Unit)?,
) {

    // Apply horizontal scroll only if user scrolling is enabled
    FlowRow(
        modifier = modifier.optionalHorizontalScroll(enabled = userScrollEnable, state = scrollState),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        data.forEach { item ->

            val decimalFormat = DecimalFormat("##.##").format(item.value)

            SuggestionChip(
                onClick = {
                    if (onItemClick != null) {
                        onItemClick(item)
                    }
                },
                label = {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(20.dp)
                            .background(item.color)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "${item.name} : $decimalFormat", style = textStyle)
                },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = item.color.copy(alpha = 0.2f)
                ),
                border = null,
                shape = CircleShape
            )
        }
    }
}

/**
 * Extension function that conditionally applies horizontal scrolling to a Modifier.
 *
 * @param enabled Boolean flag indicating whether horizontal scrolling should be applied.
 * @param state ScrollState to manage the scroll position.
 * @return The modified Modifier with or without horizontal scrolling.
 */
private fun Modifier.optionalHorizontalScroll(enabled: Boolean, state: ScrollState): Modifier {
    return if (enabled) {
        this.horizontalScroll(state)
    } else {
        this
    }
}