package com.lankasmartmart.app.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TopographicBackground(
    modifier: Modifier = Modifier,
    color: Color = Color.White.copy(alpha = 0.1f)
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val strokeStyle = Stroke(width = 1.5.dp.toPx())

        // Line 1: Top loop
        drawPath(
            path = Path().apply {
                moveTo(w * 0.1f, h * 0.1f)
                cubicTo(w * 0.3f, 0f, w * 0.5f, h * 0.2f, w * 0.2f, h * 0.3f)
                cubicTo(0f, h * 0.2f, w * 0.0f, h * 0.05f, w * 0.1f, h * 0.1f)
            },
            color = color,
            style = strokeStyle
        )

        // Line 2: Big wave across
        drawPath(
            path = Path().apply {
                moveTo(0f, h * 0.4f)
                cubicTo(w * 0.3f, h * 0.3f, w * 0.6f, h * 0.6f, w, h * 0.3f)
            },
            color = color,
            style = strokeStyle
        )

        // Line 3: Bottom organic circleish
        drawPath(
            path = Path().apply {
                moveTo(w * 0.6f, h * 0.5f)
                cubicTo(w * 0.8f, h * 0.4f, w * 0.9f, h * 0.6f, w * 0.7f, h * 0.7f)
                cubicTo(w * 0.5f, h * 0.8f, w * 0.4f, h * 0.6f, w * 0.6f, h * 0.5f)
            },
            color = color,
            style = strokeStyle
        )

        // Line 4: Long vertical curve
        drawPath(
            path = Path().apply {
                moveTo(w * 0.8f, 0f)
                cubicTo(w * 0.6f, h * 0.2f, w * 0.9f, h * 0.5f, w * 0.8f, h * 0.8f)
            },
            color = color,
            style = strokeStyle
        )
        
        // Line 5: Filler curve
        drawPath(
             path = Path().apply {
                moveTo(0f, h * 0.15f)
                cubicTo(w * 0.2f, h * 0.2f, w * 0.3f, h * 0.05f, w * 0.5f, 0f)
            },
            color = color,
            style = strokeStyle
        )
    }
}
