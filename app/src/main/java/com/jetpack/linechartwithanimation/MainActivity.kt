package com.jetpack.linechartwithanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jetpack.linechartwithanimation.ui.theme.LineChartWithAnimationTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LineChartWithAnimationTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "LineChart with Shadow",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            )
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 20.dp)
                                .background(Color.Transparent)
                        ) {
                            LineChartWithShadow(getDataPoints())
                        }
                    }
                }
            }
        }
    }
}

data class DataPoint(
    val x: Float,
    val y: Float
)

fun List<DataPoint>.xMax(): Float = maxByOrNull { it.x }?.x ?: 0f
fun List<DataPoint>.yMax(): Float = maxByOrNull { it.y }?.y ?: 0f

fun Float.toRealX(xMax: Float, width: Float) = (this / xMax) * width
fun Float.toRealY(yMax: Float, height: Float) = (this / yMax) * height

@Composable
fun LineChartWithShadow(
    dataPoint: List<DataPoint>
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(0.5f)
    ) {
        val width = size.width
        val height = size.height
        val spacingOf16DpInPixels = 16.dp.toPx()
        val verticalAxisLineStartOffset = Offset(spacingOf16DpInPixels, spacingOf16DpInPixels)
        val verticalAxisLineEndOffset = Offset(spacingOf16DpInPixels, height)

        drawLine(
            Color.Gray,
            verticalAxisLineStartOffset,
            verticalAxisLineEndOffset,
            strokeWidth = Stroke.DefaultMiter
        )

        val horizontalAxisLineStartOffset = Offset(spacingOf16DpInPixels, height)
        val horizontalAxisLineEndOffset = Offset(width - spacingOf16DpInPixels, height)

        drawLine(
            Color.Gray,
            horizontalAxisLineStartOffset,
            horizontalAxisLineEndOffset,
            strokeWidth = Stroke.DefaultMiter
        )

        val xMax = dataPoint.xMax()
        val yMax = dataPoint.yMax()
        val gradientPath = Path()

        gradientPath.moveTo(spacingOf16DpInPixels, height)
        dataPoint.forEachIndexed { index, curDataPoint ->
            var normX = curDataPoint.x.toRealX(xMax, width)
            var normY = curDataPoint.y.toRealY(yMax, height)

            if (index == 0) normX += spacingOf16DpInPixels
            if (index == dataPoint.size - 1) normX -= spacingOf16DpInPixels

            if (index < dataPoint.size - 1) {
                val offsetStart = Offset(normX, normY)
                var nextNormXPoint = dataPoint[index + 1].x.toRealX(xMax, width)

                if (index == dataPoint.size - 2)
                    nextNormXPoint = dataPoint[index + 1].x.toRealX(xMax, width = width) - spacingOf16DpInPixels

                val nextNormYPoint = dataPoint[index + 1].y.toRealY(yMax, height)
                val offsetEnd = Offset(nextNormXPoint, nextNormYPoint)

                drawLine(
                    Color(0xFFFF0000).copy(alpha = 0.5f),
                    offsetStart,
                    offsetEnd,
                    strokeWidth = Stroke.DefaultMiter
                )
            }

            drawCircle(
                Color(0xFFFF0000).copy(alpha = 0.5f),
                radius = 6.dp.toPx(),
                Offset(normX, normY)
            )
            with(
                gradientPath
            ) {
                lineTo(normX, normY)
            }
        }

        with(
            gradientPath
        ) {
            lineTo(width - spacingOf16DpInPixels, height)
            lineTo(0f, height)
            close()
            drawPath(
                this,
                brush = Brush.verticalGradient(colors = listOf(
                    Color(0xFFFF0000).copy(alpha = 0.5f),
                    Color(0xFFAFFFC)
                ))
            )
        }
    }
}

fun getDataPoints(): List<DataPoint> {
    val random = Random.Default
    return (0..10).map {
        DataPoint(it.toFloat(), random.nextInt(50).toFloat() + 1f)
    }
}






















