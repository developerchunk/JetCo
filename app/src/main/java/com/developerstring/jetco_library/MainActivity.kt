package com.developerstring.jetco_library

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.developerstring.jetco_library.ui.theme.Pink40
import com.developerstring.jetco_library.ui.theme.Purple80
import com.developerstring.jetco_library.ui.theme.PurpleGrey80
import com.developerstring.jetco.ui.charts.barchart.ColumnBarChart
import com.developerstring.jetco.ui.charts.barchart.ExtendedColumnBarChart
import com.developerstring.jetco.ui.charts.barchart.GroupColumnBarChart
import com.developerstring.jetco.ui.charts.piechart.PieChart
import com.developerstring.jetco.ui.charts.piechart.config.PieChartDefaults
import com.developerstring.jetco_library.ui.theme.JetCoLibraryTheme
import java.text.DecimalFormat

val UIBlue = Color(0xFF1E90FF)
val LightBlue = Color(0xFFB5DAFF)
val LightestPink = Color(0xFFF7F1FF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetCoLibraryTheme {

                // The following sample code demonstrates the usage of four different chart components available in this library:
                // 1. Pie Chart: A circular chart divided into slices to illustrate numerical proportions.
                // 2. Column Bar Chart: A vertical bar chart that displays data with rectangular bars representing values.
                // 3. Extended Column Bar Chart: An enhanced version of the Column Bar Chart with additional customization options for UI and state management.
                // 4. Group Column Bar Chart: A chart that displays multiple data series grouped together in a vertical bar format, allowing comparison across different categories.

                val state = rememberScrollState()

                val chartData = mapOf(
                    Pair("Test-1", 25f),
                    Pair("Test-2:beta", 50f),
                    Pair("Test-3", 80f),
                    Pair("Test-4", 75f),
                    Pair("Test-5", 60f),
                    Pair("Test-6", 25f),
                    Pair("Test-7", 50f),
                    Pair("Test-8", 80f),
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(state)
                        .padding(vertical = 20.dp, horizontal = 30.dp)
                ) {

                    PieChart(
                        modifier = Modifier.fillMaxWidth(),
                        chartData = chartData,
                        pieChartConfig = PieChartDefaults.pieChartConfig(
                            isChartItemScrollEnable = false
                        )
                    )

                    ColumnBarChart(
                        modifier = Modifier,
                        chartData = chartData,
                        maxBarValue = 100f,
                    )

                    ExtendedColumnBarChart(
                        modifier = Modifier,
                        chartData = chartData,
                        maxTextLengthXAxis = 6,
                        maxBarValue = 100f,
                        yAxisScaleLabel = { value ->
                            Card(
                                colors = CardDefaults.cardColors(PurpleGrey80),
                                shape = RoundedCornerShape(20)
                            ) {
                                Text(text = value)
                            }
                        },
                        gridLine = {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .background(LightestPink)
                            ) {
                                val configuration = LocalConfiguration.current
                                val screenWidthDp = configuration.screenWidthDp

                                CurvyWaveLine(
                                    pathColor = Purple80,
                                    modifier = Modifier.fillMaxSize(),
                                    numWaves = screenWidthDp / 25
                                )
                            }
                        },
                        barPopUp = { text ->
                            val value = try {
                                DecimalFormat("##.##").format(text.toFloat()).toString()
                            } catch (_: Exception) {
                                text
                            }
                            PopUpLayout(text = value)
                        },
                        labelPopUp = { text ->
                            PopUpLayout(text = text)
                        },
                        barDesign = { text ->
                            val decimalFormat = DecimalFormat("##.##").format(text.toFloat())
                            Card(
                                modifier = Modifier.fillMaxSize(),
                                colors = CardDefaults.cardColors(containerColor = Pink40),
                                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 20.dp)
                            ) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = decimalFormat,
                                    modifier = Modifier.rotate(-90f),
                                    color = Color.White
                                )
                            }
                        }
                    )

                    GroupColumnBarChart(
                        modifier = Modifier
                            .fillMaxWidth(),
                        chartData = mapOf(
                            "Test-1" to listOf(65f, 50f, 40f, 30f),
                            "Test-2" to listOf(75f, 25f, 50f, 60f),
                            "Test-3" to listOf(65f, 50f, 40f, 55f),
                        ),
                        maxBarValue = 100f,
                    )

                }

            }
        }
    }
}

@Composable
fun PopUpLayout(text: String) {

    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(100))
            .background(LightBlue)
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .clip(shape = RoundedCornerShape(100))
                .background(UIBlue),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(5.dp),
                text = text,
                color = Color.White
            )
        }
    }

}

@Composable
fun CurvyWaveLine(
    modifier: Modifier = Modifier,
    pathColor: Color = Color.Black,
    pathWidth: Dp = 2.dp,
    amplitude: Dp = 10.dp,
    numWaves: Int = 30
) {

    val path = remember { androidx.compose.ui.graphics.Path() }

    path.moveTo(0f, 0f)

    Canvas(
        modifier = modifier
            .clipToBounds()
            .padding(top = pathWidth * 2f)
    ) {

        val waveHeight = amplitude.toPx()
        val waveWidth = size.height / 2

        for (i in 0 until numWaves) {
            val startX = i * waveWidth
            val endX = startX + waveWidth

            path.cubicTo(
                startX + waveWidth / 4, waveHeight,
                startX + 3 * waveWidth / 4, -waveHeight,
                endX, 0f
            )
        }

        drawPath(
            path = path,
            color = pathColor,
            style = Stroke(width = pathWidth.toPx(), cap = StrokeCap.Round)
        )
    }
}