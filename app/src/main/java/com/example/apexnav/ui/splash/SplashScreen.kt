package com.example.apexnav.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apexnav.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onAnimationEnd: () -> Unit) {

    // Logo scale-in
    val logoScale by animateFloatAsState(
        targetValue  = 1f,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "logoScale"
    )
    // Text fade-in
    val textAlpha by animateFloatAsState(
        targetValue  = 1f,
        animationSpec = tween(900, delayMillis = 400),
        label = "textAlpha"
    )
    // Tagline slide
    val taglineOffset by animateDpAsState(
        targetValue  = 0.dp,
        animationSpec = tween(700, delayMillis = 600, easing = FastOutSlowInEasing),
        label = "taglineOffset"
    )

    // Glow pulse
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 0.7f,
        animationSpec = infiniteRepeatable(tween(1200), RepeatMode.Reverse),
        label = "glowAlpha"
    )

    LaunchedEffect(Unit) {
        delay(2400)
        onAnimationEnd()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark),
        contentAlignment = Alignment.Center
    ) {
        // Radial glow background
        Box(
            modifier = Modifier
                .size(320.dp)
                .alpha(glowAlpha)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            ApexCyan.copy(alpha = 0.15f),
                            Color.Transparent,
                        )
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Diamond logo
            Box(
                modifier = Modifier
                    .scale(logoScale)
                    .size(96.dp)
                    .background(
                        Brush.linearGradient(listOf(ApexCyan, ApexBlue)),
                        shape = androidx.compose.foundation.shape.GenericShape { size, _ ->
                            val w = size.width; val h = size.height
                            moveTo(w / 2f, 0f)
                            lineTo(w, h / 2f)
                            lineTo(w / 2f, h)
                            lineTo(0f, h / 2f)
                            close()
                        }
                    )
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text     = "ApexNav",
                    modifier = Modifier.alpha(textAlpha),
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color    = TextPrimary,
                    letterSpacing = (-1).sp,
                )
                Text(
                    text     = "Smart Navigation · Malaysia",
                    modifier = Modifier
                        .alpha(textAlpha)
                        .offset(y = taglineOffset),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color    = ApexCyan,
                    letterSpacing = 0.5.sp,
                )
            }
        }
    }
}
