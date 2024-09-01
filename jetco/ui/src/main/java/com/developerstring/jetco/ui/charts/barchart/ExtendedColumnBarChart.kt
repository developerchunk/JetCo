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
import androidx.compose.ui.window.Popup
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
 * A highly customizable and animated column bar chart that supports various design and configuration options
 * including Y-axis and X-axis labels, grid lines, pop-ups, and user interactions.
 *
 * @param modifier Modifier to be applied to the ColumnBarChart container.
 * @param chartData A map containing the chart data, where the key is the label for each bar (X-axis)
 * and the value is the corresponding float value represents the data-value of the bar.
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
 * @param maxBarValue Optional maximum value for the bars. If null, the maximum value from the chart data is used.
 * @param enableAnimation Flag to enable or disable the animation of the bars when the chart is displayed.
 * @param enterAnimation The animation to be used when the bars enter the screen.
 * @param exitAnimation The animation to be used when the bars exit the screen.
 * @param maxTextLengthXAxis Maximum length of the text labels on the X-axis before truncation occurs.
 * @param enableTextRotate Flag to enable or disable rotation of the X-axis labels. Useful for longer labels.
 * @param textRotateAngle The angle of rotation for the X-axis labels if rotation is enabled.
 * @param enableGridLines Flag to enable or disable the display of grid lines behind the bars.
 * @param scrollEnable Flag to enable or disable horizontal scrolling for the chart when the content exceeds the available width.
 * @param onBarClicked Optional callback triggered when a bar is clicked, providing the corresponding X-axis label and Y-axis value.
 * @param onXAxisLabelClicked Optional callback triggered when an X-axis label is clicked, providing the corresponding X-axis label and Y-axis value.
 * @param barDesign Optional custom Composable to design the appearance of the bars. If null, a default design is used.
 * @param gridLine Optional custom Composable to design the grid lines. If null, a default grid line design is used.
 * @param barPopUp Optional custom Composable to design the bar pop-up that appears when a bar is clicked. If null, a default pop-up design is used.
 * @param labelPopUp Optional custom Composable to design the label pop-up that appears when a label is clicked. If null, a default pop-up design is used.
 * @param yAxisScaleLabel Optional custom Composable to design the Y-axis scale labels. If null, a default label design is used.
 *
 * @see BarChartDefaults
 *
 */
@Composable
fun ExtendedColumnBarChart(
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
    onXAxisLabelClicked: ((Pair<String, Float>) -> Unit)? = null,
    barDesign: (@Composable (text: String) -> Unit)? = null,
    gridLine: (@Composable () -> Unit)? = null,
    barPopUp: (@Composable (text: String) -> Unit)? = null,
    labelPopUp: (@Composable (text: String) -> Unit)? = null,
    yAxisScaleLabel: (@Composable (value: String) -> Unit)? = null,
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
                    if (yAxisScaleLabel != null) {
                        Row {
                            // Column for the Y-axis scale labels
                            Column(horizontalAlignment = Alignment.End) {
                                repeat(yAxisConfig.axisScaleCount + 1) { index ->
                                    val barScale = (yAxisConfig.axisScaleCount) - index
                                    Row(
                                        modifier = Modifier.height(height = yAxisStepHeight),
                                        verticalAlignment = Alignment.Bottom
                                    ) {
                                        yAxisScaleLabel((yAxisScaleStep * barScale).toString())
                                    }
                                }
                            }

                            // Y-axis line
                            if (yAxisConfig.isAxisLineEnabled) {
                                Spacer(modifier = Modifier.width(10.dp))

                                Box(
                                    modifier = Modifier
                                        .padding(top = yAxisStepHeight)
                                        .clip(shape = yAxisConfig.axisLineShape)
                                        .width(yAxisConfig.axisLineWidth)
                                        .height(barChartConfig.height)
                                        .background(yAxisConfig.axisLineColor)
                                )
                            }
                        }
                    } else {
                        YAxisScale(
                            yAxisConfig = yAxisConfig,
                            yAxisStepHeight = yAxisStepHeight,
                            yAxisScaleStep = yAxisScaleStep,
                            barHeight = barChartConfig.height
                        )
                    }
                }

                Box(modifier = Modifier.fillMaxWidth()) {

                    // grid lines
                    if (enableGridLines) {

                        if (gridLine != null) {
                            Column(
                                modifier = Modifier
                                    .padding(top = yAxisStepHeight)
                                    .fillMaxWidth()
                            ) {
                                repeat(gridLineStyle.totalGridLines + 1) { index ->
                                    if (index != 0) {
                                        Column(modifier = Modifier.height(yAxisStepHeight)) {
                                            gridLine()
                                        }
                                    }
                                }
                            }
                        } else {
                            YAxisGridLines(
                                gridLineStyle = gridLineStyle,
                                yAxisStepHeight = yAxisStepHeight
                            )
                        }
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

                            // Bar UI
                            Column(
                                modifier = Modifier
                                    .wrapContentSize(),
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                if (isBarPopupVisible && popUpConfig.enableBarPopUp) {
                                    val decimalFormat = DecimalFormat("##.##")
                                    if (barPopUp != null) {
                                        Popup(
                                            alignment = Alignment.Center,
                                            onDismissRequest = {
                                                isBarPopupVisible = false
                                            },
                                        ) {
                                            barPopUp(barPopupText.toString())
                                        }
                                    } else {
                                        BarChartPopup(
                                            popUpConfig = popUpConfig,
                                            text = decimalFormat.format(barPopupText),
                                            onDismissRequest = {
                                                isBarPopupVisible = false
                                            }
                                        )
                                    }
                                }

                                if (isYAxisPopupVisible && popUpConfig.enableXAxisPopUp) {
                                    if (labelPopUp != null) {
                                        Popup(
                                            alignment = Alignment.Center,
                                            onDismissRequest = {
                                                isYAxisPopupVisible = false
                                            },
                                        ) {
                                            labelPopUp(yAxisPopupText)
                                        }
                                    } else {
                                        BarChartPopup(
                                            popUpConfig = popUpConfig,
                                            text = yAxisPopupText,
                                            onDismissRequest = {
                                                isYAxisPopupVisible = false
                                            }
                                        )
                                    }
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

                                        val clickable: () -> Unit = {
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

                                        if(barDesign != null) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxHeight(barItem.floatValue)
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        clickable()
                                                    },
                                                contentAlignment = Alignment.BottomCenter
                                            ) {

                                                barDesign(barItem.value.toString())

                                            }
                                        } else {
                                            Box(
                                                modifier = Modifier
                                                    .clip(shape = barChartConfig.shape)
                                                    .fillMaxHeight(barItem.floatValue)
                                                    .fillMaxWidth()
                                                    .background(
                                                        barChartConfig.color
                                                    )
                                                    .clickable {
                                                        clickable()
                                                    }
                                            )
                                        }
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

                                    val itemName = if (barItem.name.length > maxTextLengthXAxis) "${
                                        barItem.name.take(maxTextLengthXAxis - 2)
                                    }..." else barItem.name

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