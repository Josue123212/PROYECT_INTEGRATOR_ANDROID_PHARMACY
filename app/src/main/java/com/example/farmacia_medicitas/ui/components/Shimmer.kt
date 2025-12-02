package com.example.farmacia_medicitas.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun shimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val offsetX by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(animation = tween(durationMillis = 1200, easing = LinearEasing)),
        label = "offset"
    )
    return Brush.linearGradient(
        colors = listOf(
            Color(0xFFE9E9E9),
            Color(0xFFF5F5F5),
            Color(0xFFE9E9E9)
        ),
        start = androidx.compose.ui.geometry.Offset(offsetX, 0f),
        end = androidx.compose.ui.geometry.Offset(offsetX + 200f, 200f)
    )
}
