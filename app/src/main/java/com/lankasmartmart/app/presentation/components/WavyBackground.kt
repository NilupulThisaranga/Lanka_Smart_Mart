package com.lankasmartmart.app.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lankasmartmart.app.ui.theme.PurplePrimary
import com.lankasmartmart.app.ui.theme.PurpleLight

@Composable
fun WavyBackground(
    modifier: Modifier = Modifier,
    height: Dp = 300.dp
) {
    Box(modifier = modifier.fillMaxWidth().height(height)) {
        Canvas(modifier = Modifier.fillMaxWidth().height(height)) {
            val width = size.width
            val heightPx = size.height

            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(0f, heightPx * 0.7f)
                
                // First curve
                cubicTo(
                    width * 0.2f, heightPx * 0.8f,
                    width * 0.4f, heightPx * 0.5f,
                    width * 0.6f, heightPx * 0.65f
                )
                
                // Second curve
                cubicTo(
                    width * 0.8f, heightPx * 0.8f,
                    width * 0.9f, heightPx * 0.75f,
                    width, heightPx * 0.6f
                )
                
                lineTo(width, 0f)
                close()
            }

            drawPath(
                path = path,
                brush = Brush.linearGradient(
                    colors = listOf(PurpleLight, PurplePrimary),
                    start = Offset(0f, 0f),
                    end = Offset(0f, heightPx)
                )
            )
            
            // Draw subtle contour lines (optional for "map-like" effect)
            val linePath = Path().apply {
                 moveTo(0f, 0f)
                 // Simple decorative curve
                 cubicTo(
                    width * 0.3f, heightPx * 0.3f,
                    width * 0.7f, heightPx * 0.4f,
                    width, heightPx * 0.2f
                )
            }
             drawPath(
                path = linePath,
                color = Color.White.copy(alpha = 0.1f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
            )
        }
    }
}
