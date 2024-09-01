package com.developerstring.jetco.ui.charts.barchart

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.developerstring.jetco.ui.charts.barchart.components.BarChartPopup
import com.developerstring.jetco.ui.charts.barchart.components.XAxisLabel
import com.developerstring.jetco.ui.charts.barchart.components.YAxisGridLines
import com.developerstring.jetco.ui.charts.barchart.components.YAxisScale
import com.developerstring.jetco.ui.charts.barchart.config.BarChartConfig
import com.developerstring.jetco.ui.charts.barchart.config.BarChartDefaults
import com.developerstring.jetco.ui.charts.barchart.config.GridLineStyle
import com.developerstring.jetco.ui.charts.barchart.config.PopUpConfig
import com.developerstring.jetco.ui.charts.barchart.config.XAxisConfig
import com.developerstring.jetco.ui.charts.barchart.config.YAxisConfig
import com.developerstring.jetco.ui.charts.barchart.model.mapToBarChartItems
import java.text.DecimalFormat

/**
 * Composable function that displays a column bar chart.
 *
 * This chart represents data using vertical bars with various customization options including
 * animation, axis configurations, and pop-up information. It supports features like axis scaling,
 * grid lines, and bar pop-ups for interactive data visualization.
 *
 * @param modifier Modifier to be applied to the entire chart.
 * @param chartData A map containing the data to be displayed, where each key represents a label on the X-axis
 *                  and the corresponding value represents the data-value of the bar.
 * @param barChartConfig Configuration for the appearance of the bars, such as height, width, shape, and color.
 * Default configuration can be provided via [BarChartDefaults.columnBarChartConfig].
 * @param yAxisConfig Configuration for the Y-axis, including scale, line properties, and labels.
 * Default configuration can be provided via [BarChartDefaults.yAxisConfig].
 * @param xAxisConfig Configuration for the X-axis, including scale, line properties, and labels.
 * Default configuration can be provided via [BarChartDefaults.xAxisConfig].
 * @param popUpConfig Configuration for the pop-ups that appear on bar or label clicks. Default configuration can be
 * provided via [BarChartDefaults.popUpConfig].
 * @param gridLineStyle Configuration for the grid lines displayed behind the bars.
 * Default configuration can be provided via [BarChartDefaults.gridLineStyle].
 * @param maxBarValue Optional maximum value for scaling the bars; if not provided, the maximum value from
 *                    the data will be used.
 * @param enableAnimation Flag to enable or disable bar animations.
 * @param enterAnimation Transition effect when bars enter the screen.
 * @param exitAnimation Transition effect when bars exit the screen.
 * @param maxTextLengthXAxis Maximum length of the text label on the X-axis; longer texts will be truncated.
 * @param enableTextRotate Flag to enable or disable rotation of X-axis labels.
 * @param textRotateAngle Angle for rotating X-axis labels.
 * @param enableGridLines Flag to enable or disable grid lines on the chart.
 * @param scrollEnable Flag to enable or disable horizontal scrolling of the chart.
 * @param onBarClicked Optional callback invoked when a bar is clicked, passing the bar's label and value.
 * @param onXAxisLabelClicked Optional callback invoked when an X-axis label is clicked, passing the label and value.
 *
 * @see BarChartDefaults
 *
 */
@Composable
fun ColumnBarChart(
    modifier: Modifier = Modifier,
    chartData: Map<String, Float>,
    barChartConfig: BarChartConfig = BarChartDefaults.columnBarChartConfig(),
    yAxisConfig: YAxisConfig = BarChartDefaults.yAxisConfig(),
    xAxisConfig: XAxisConfig = BarChartDefaults.xAxisConfig(),
    popUpConfig: PopUpConfig = BarChartDefaults.popUpConfig(),
    gridLineStyle: GridLineStyle = BarChartDefaults.gridLineStyle(),
    maxBarValue: Float? = null,
    enableAnimation: Boolean = true,
    enterAnimation: EnterTransition = BarChartDefaults.enterTransitionVertically,
    exitAnimation: ExitTransition = shrinkVertically(),
    maxTextLengthXAxis: Int = 6,
    enableTextRotate: Boolean = true,
    textRotateAngle: Float = -60f,
    enableGridLines: Boolean = true,
    scrollEnable: Boolean = true,
    onBarClicked: ((Pair<String, Float>) -> Unit)? = null,
    onXAxisLabelClicked: ((Pair<String, Float>) -> Unit)? = null
) {

    // Determine the maximum value in the data set, or use the provided max value
    val maxValue: Float = maxBarValue ?: chartData.values.maxOrNull() ?: 0f

    // Transform the data into a list of BarChartItem with normalized / float heights
    val barList = chartData.mapToBarChartItems(maxValue = maxValue)

    // State for managing whether the animation should be displayed
    var animationDisplay by remember {
        mutableStateOf(!enableAnimation)
    }

    // Trigger the animation when the composable is first launched
    if (enableAnimation) {
        LaunchedEffect(key1 = Unit) {
            animationDisplay = true
        }
    }

    // Calculate the Y-axis scale step based on the maximum value and the number of steps
    val yAxisScaleStep = maxValue / yAxisConfig.axisScaleCount
    val yAxisStepHeight = barChartConfig.height / yAxisConfig.axisScaleCount

    Column(modifier = modifier) {

        Box(contentAlignment = Alignment.TopStart) {

            // Y-axis and bars container
            Row(horizontalArrangement = Arrangement.Start) {

                // Y-axis scale and line
                if (yAxisConfig.isAxisScaleEnabled) {
                    YAxisScale(
                        yAxisConfig = yAxisConfig,
                        yAxisStepHeight = yAxisStepHeight,
                        yAxisScaleStep = yAxisScaleStep,
                        barHeight = barChartConfig.height
                    )
                }

                Box(modifier = Modifier.fillMaxWidth()) {

                    // grid lines
                    if (enableGridLines) {
                        YAxisGridLines(
                            gridLineStyle = gridLineStyle,
                            yAxisStepHeight = yAxisStepHeight
                        )
                    }

                    // Bars and X-axis labels container
                    LazyRow(
                        modifier = Modifier
                            .padding(top = yAxisStepHeight)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        userScrollEnabled = scrollEnable
                    ) {
                        items(barList, key = { keyItem ->
                            keyItem.name
                        }) { barItem ->

                            // State for managing the popup
                            var isYAxisPopupVisible by remember { mutableStateOf(false) }
                            var yAxisPopupText by remember { mutableStateOf("") }

                            // State for managing the popup for each bar
                            var isBarPopupVisible by remember { mutableStateOf(false) }
                            var barPopupText by remember { mutableFloatStateOf(0f) }

                            val barItemLength = barItem.name.length

                            // Bar UI
                            Column(
                                modifier = Modifier
                                    .wrapContentSize(),
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                if (isBarPopupVisible && popUpConfig.enableBarPopUp) {
                                    val decimalFormat = DecimalFormat("##.##")
                                    BarChartPopup(
                                        popUpConfig = popUpConfig,
                                        text = decimalFormat.format(barPopupText),
                                        onDismissRequest = {
                                            isBarPopupVisible = false
                                        }
                                    )
                                }

                                if (isYAxisPopupVisible && popUpConfig.enableXAxisPopUp) {
                                    BarChartPopup(
                                        popUpConfig = popUpConfig,
                                        text = yAxisPopupText,
                                        onDismissRequest = {
                                            isYAxisPopupVisible = false
                                        }
                                    )
                                }

                                // Bar Box
                                Box(
                                    modifier = Modifier
                                        .height(barChartConfig.height)
                                        .width(barChartConfig.width),
                                    contentAlignment = Alignment.BottomCenter
                                ) {

                                    // Lambda to create the bar with the specified shape, color, and click handling
                                    val barBox: @Composable () -> Unit = {

                                        Box(
                                            modifier = Modifier
                                                .clip(shape = barChartConfig.shape)
                                                .fillMaxHeight(barItem.floatValue)
                                                .fillMaxWidth()
                                                .background(
                                                    barChartConfig.color
                                                )
                                                .clickable {
                                                    if (popUpConfig.enableBarPopUp) {
                                                        barPopupText = barItem.value
                                                        isBarPopupVisible = true
                                                    }

                                                    if (onBarClicked != null) {
                                                        onBarClicked(
                                                            Pair(
                                                                barItem.name,
                                                                barItem.value
                                                            )
                                                        )
                                                    }
                                                }
                                        )
                                    }

                                    // Animate the bar if animations are enabled
                                    if (enableAnimation) {
                                        androidx.compose.animation.AnimatedVisibility(
                                            visible = animationDisplay,
                                            enter = enterAnimation,
                                            exit = exitAnimation
                                        ) {
                                            barBox()
                                        }
                                    } else {
                                        barBox()
                                    }
                                }
                                // X-axis labels below each bar
                                if (xAxisConfig.isAxisScaleEnabled) {
                                    Spacer(modifier = Modifier.height(xAxisConfig.axisLineWidth + 5.dp))

                                    val itemName = if (barItemLength > maxTextLengthXAxis) "${
                                        barItem.name.take(if (maxTextLengthXAxis>5) (maxTextLengthXAxis-2) else maxTextLengthXAxis)
                                    }.." else barItem.name

                                    Row {
                                        if (enableTextRotate) {
                                            Spacer(modifier = Modifier.width(barChartConfig.width / 4))
                                        }
                                        XAxisLabel(
                                            itemName = itemName,
                                            xAxisTextStyle = xAxisConfig.textStyle,
                                            textRotateAngle = textRotateAngle,
                                            enableTextRotate = enableTextRotate,
                                            onLabelClick = {
                                                if (popUpConfig.enableXAxisPopUp) {
                                                    yAxisPopupText = barItem.name
                                                    isYAxisPopupVisible = true
                                                }

                                                if (onXAxisLabelClicked != null) {
                                                    onXAxisLabelClicked(
                                                        Pair(
                                                            barItem.name,
                                                            barItem.value
                                                        )
                                                    )
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // X-axis line at the bottom of the chart
            if (xAxisConfig.isAxisLineEnabled) {
                Box(
                    modifier = Modifier
                        .padding(top = barChartConfig.height + yAxisStepHeight)
                        .fillMaxWidth()
                        .height(xAxisConfig.axisLineWidth)
                        .clip(shape = xAxisConfig.axisLineShape)
                        .background(xAxisConfig.axisLineColor)
                )

            }

        }
    }
}