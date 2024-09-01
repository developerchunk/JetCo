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
import com.developerstring.jetco.ui.charts.barchart.config.BarChartDefaults
import com.developerstring.jetco.ui.charts.barchart.config.GridLineStyle
import com.developerstring.jetco.ui.charts.barchart.config.GroupBarChartConfig
import com.developerstring.jetco.ui.charts.barchart.config.PopUpConfig
import com.developerstring.jetco.ui.charts.barchart.config.XAxisConfig
import com.developerstring.jetco.ui.charts.barchart.config.YAxisConfig
import com.developerstring.jetco.ui.charts.barchart.model.mapToGroupBarChartItems
import java.text.DecimalFormat

/**
 * GroupColumnBarChart displays a group column bar chart, where each group contains multiple bars
 * representing different categories. It is highly customizable, supporting various UI configurations
 * like axis styles, grid lines, animations, popups, and more.
 *
 * @param modifier Modifier to be applied to the GroupColumnBarChart layout.
 * @param chartData Data to be displayed in the chart, mapped by group labels to a list of values
 * representing the bars within each group.
 * @param groupBarChartConfig Configuration for the appearance of the group column bar chart, including
 * bar colors, shapes, and dimensions. Defaults to a configuration provided by [BarChartDefaults.groupBarChartConfig].
 * @param yAxisConfig Configuration for the Y-axis, including scale steps, line visibility, and styles.
 * Defaults to a configuration provided by [BarChartDefaults.yAxisConfig].
 * @param xAxisConfig Configuration for the X-axis, including label styles, rotation, and line visibility.
 * Defaults to a configuration provided by [BarChartDefaults.xAxisConfig].
 * @param popUpConfig Configuration for the pop-up that appears when bars or axis labels are clicked.
 * Defaults to a configuration provided by [BarChartDefaults.popUpConfig].
 * @param gridLineStyle Configuration for the grid lines that appear behind the bars. Defaults to a configuration
 * provided by [BarChartDefaults.gridLineStyle].
 * @param maxBarValue Maximum value for the Y-axis. If null, it is determined from the chartData. Defaults to null.
 * @param enableAnimation Boolean flag to enable or disable the animation when the chart first appears.
 * @param enterAnimation Enter transition for the bar animations. Defaults to [BarChartDefaults.enterTransitionVertically].
 * @param exitAnimation Exit transition for the bar animations.
 * @param maxTextLengthXAxis Maximum number of characters for X-axis labels before truncating with ellipsis.
 * @param enableTextRotate Boolean flag to enable or disable rotation of the X-axis labels.
 * @param textRotateAngle Rotation angle for the X-axis labels when [enableTextRotate] is true.
 * @param enableGridLines Boolean flag to enable or disable grid lines.
 * @param scrollEnable Boolean flag to enable or disable horizontal scrolling for the chart.
 * @param onBarClicked Callback function that is triggered when a bar is clicked, returning a pair containing
 * the label of the group and the value of the clicked bar.
 * @param onXAxisLabelClicked Callback function that is triggered when an X-axis label is clicked, returning
 * a pair containing the label of the group and the list of values corresponding to that group. Defaults to null.
 *
 * @see BarChartDefaults
 *
 */
@Composable
fun GroupColumnBarChart(
    modifier: Modifier = Modifier,
    chartData: Map<String, List<Float>>,
    groupBarChartConfig: GroupBarChartConfig = BarChartDefaults.groupBarChartConfig(),
    yAxisConfig: YAxisConfig = BarChartDefaults.yAxisConfig(),
    xAxisConfig: XAxisConfig = BarChartDefaults.xAxisConfig(),
    popUpConfig: PopUpConfig = BarChartDefaults.popUpConfig(),
    gridLineStyle: GridLineStyle = BarChartDefaults.gridLineStyle(),
    maxBarValue: Float? = null,
    enableAnimation: Boolean = true,
    enterAnimation: EnterTransition = BarChartDefaults.enterTransitionVertically,
    exitAnimation: ExitTransition = shrinkVertically(),
    maxTextLengthXAxis: Int = 6,
    enableTextRotate: Boolean = false,
    textRotateAngle: Float = -60f,
    enableGridLines: Boolean = true,
    scrollEnable: Boolean = true,
    onBarClicked: ((Pair<String, Float>) -> Unit)? = null,
    onXAxisLabelClicked: ((Pair<String, List<Float>>) -> Unit)? = null
) {

    // Determine the maximum value in the data set, or use the provided max value
    val maxValue: Float = maxBarValue ?: chartData.values.flatten().maxOrNull() ?: 0f

    // Transform the data into a list of BarChartItem with normalized / float heights
    val barLists = chartData.mapToGroupBarChartItems(maxValue = maxValue)

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
    val yAxisStepHeight = groupBarChartConfig.height / yAxisConfig.axisScaleCount

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
                        barHeight = groupBarChartConfig.height
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

                    // Group Bars and X-axis labels container
                    LazyRow(
                        modifier = Modifier
                            .padding(top = yAxisStepHeight)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        userScrollEnabled = scrollEnable
                    ) {
                        items(barLists, key = { keyItem ->
                            keyItem.name
                        }) { barItem ->

                            // State for managing the popup
                            var isYAxisPopupVisible by remember { mutableStateOf(false) }
                            var yAxisPopupText by remember { mutableStateOf("") }

                            // Bar UI
                            Column(
                                modifier = Modifier
                                    .padding(start = groupBarChartConfig.gapBetweenGroup)
                                    .wrapContentSize(),
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                if (isYAxisPopupVisible && popUpConfig.enableXAxisPopUp) {
                                    BarChartPopup(
                                        popUpConfig = popUpConfig,
                                        text = yAxisPopupText,
                                        onDismissRequest = {
                                            isYAxisPopupVisible = false
                                        }
                                    )
                                }

                                // All bars of each group will be in this Row
                                Row(
                                    modifier = Modifier
                                        .wrapContentSize(),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                ) {

                                    barItem.barValues.forEachIndexed { index, barValue ->

                                        // State for managing the popup for each bar
                                        var isBarPopupVisible by remember { mutableStateOf(false) }
                                        var barPopupText by remember { mutableFloatStateOf(0f) }

                                        Box(
                                            modifier = Modifier
                                                .padding(horizontal = groupBarChartConfig.gapBetweenBar / 2)
                                                .height(groupBarChartConfig.height)
                                                .width(groupBarChartConfig.width),
                                            contentAlignment = Alignment.BottomCenter
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

                                            // Lambda to create the bar with the specified shape, color, and click handling
                                            val barBox: @Composable () -> Unit = {

                                                Box(
                                                    modifier = Modifier
                                                        .clip(shape = groupBarChartConfig.shape)
                                                        .fillMaxHeight(barValue.floatValue)
                                                        .fillMaxWidth()
                                                        .background(
                                                            color = groupBarChartConfig.colors.getOrElse(
                                                                index = index,
                                                                defaultValue = { groupBarChartConfig.colors.last() }
                                                            )
                                                        )
                                                        .clickable {
                                                            if (popUpConfig.enableBarPopUp) {
                                                                barPopupText =
                                                                    barValue.value
                                                                isBarPopupVisible = true
                                                            }

                                                            if (onBarClicked != null) {
                                                                onBarClicked(
                                                                    Pair(
                                                                        barItem.name,
                                                                        barValue.value
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
                                    }
                                }
                                // X-axis labels below each bar
                                if (xAxisConfig.isAxisScaleEnabled) {
                                    Spacer(modifier = Modifier.height(xAxisConfig.axisLineWidth + 5.dp))

                                    val itemName =
                                        if (barItem.name.length > maxTextLengthXAxis) "${
                                            barItem.name.take(maxTextLengthXAxis - 2)
                                        }..." else barItem.name

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
                                                        barItem.barValues.map { it.value }
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

            // X-axis line at the bottom of the chart
            if (xAxisConfig.isAxisLineEnabled) {
                Box(
                    modifier = Modifier
                        .padding(top = groupBarChartConfig.height + yAxisStepHeight)
                        .fillMaxWidth()
                        .height(xAxisConfig.axisLineWidth)
                        .clip(shape = xAxisConfig.axisLineShape)
                        .background(xAxisConfig.axisLineColor)
                )

            }

        }
    }
}