package com.example.apexnav.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TurnLeft
import androidx.compose.material.icons.filled.TurnRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apexnav.ActiveNav
import com.example.apexnav.data.LaneInfo
import com.example.apexnav.data.LaneType
import com.example.apexnav.data.SCENARIO_KLCC
import com.example.apexnav.data.WeatherInfo
import com.example.apexnav.theme.ApexAmber
import com.example.apexnav.theme.ApexBlue
import com.example.apexnav.theme.ApexCrimson
import com.example.apexnav.theme.ApexCyan
import com.example.apexnav.theme.ApexEmerald
import com.example.apexnav.theme.BorderSubtle
import com.example.apexnav.theme.SurfaceDark
import com.example.apexnav.theme.SurfaceElevated
import com.example.apexnav.theme.SurfacePanel
import com.example.apexnav.theme.TextMuted
import com.example.apexnav.theme.TextPrimary
import com.example.apexnav.theme.TextSecondary
import com.example.apexnav.ui.components.MapCtrlButton
import kotlinx.coroutines.delay
import java.util.Calendar

@Composable
fun ActiveNavScreen(navKey: ActiveNav, onEndNav: () -> Unit) {
    var currentSpeedKmh by remember { mutableFloatStateOf(0f) }
    var targetSpeedKmh  by remember { mutableFloatStateOf(95f) }
    var isDriving       by remember { mutableStateOf(false) }
    var isBoost         by remember { mutableStateOf(false) }
    var turnDistanceM   by remember { mutableIntStateOf(SCENARIO_KLCC.maneuverDistanceM) }
    var radarVisible    by remember { mutableStateOf(false) }
    var radarDistM      by remember { mutableIntStateOf(0) }
    var radarPct        by remember { mutableFloatStateOf(1f) }
    var arrivalTime     by remember { mutableStateOf("") }

    LaunchedEffect(isDriving) {
        if (!isDriving) return@LaunchedEffect
        while (isDriving) {
            currentSpeedKmh += (targetSpeedKmh - currentSpeedKmh) * 0.08f
            if (turnDistanceM > 50) turnDistanceM -= 3
            val simRadarDist = (500 - ((SCENARIO_KLCC.maneuverDistanceM - turnDistanceM) * 1.2)).toInt()
            if (simRadarDist in 30..500) {
                radarVisible = true
                radarDistM   = simRadarDist.coerceAtLeast(30)
                radarPct     = simRadarDist / 500f
            } else {
                radarVisible = false
            }
            val cal = Calendar.getInstance()
            cal.add(Calendar.MINUTE, navKey.etaMinutes)
            arrivalTime = "%02d:%02d".format(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
            delay(150)
        }
    }

    LaunchedEffect(Unit) {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MINUTE, navKey.etaMinutes)
        arrivalTime = "%02d:%02d".format(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF0F1924), Color(0xFF07090E)),
                    radius = 1400f,
                )
            )
    ) {
        // Speed camera radar (slides in from right)
        AnimatedVisibility(
            visible  = radarVisible,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 100.dp, end = 16.dp),
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
            exit  = slideOutHorizontally(targetOffsetX = { it }) + fadeOut(),
        ) {
            RadarAlertCard(
                limitKmh        = SCENARIO_KLCC.camera.limitKmh,
                distanceM       = radarDistM,
                progressPct     = radarPct,
                isOverspeed     = currentSpeedKmh > SCENARIO_KLCC.camera.limitKmh,
                currentSpeedKmh = currentSpeedKmh.toInt(),
            )
        }

        // Maneuver HUD + Lane guidance
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .widthIn(max = 340.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            ManeuverHud(distanceM = turnDistanceM)
            LaneGuidanceCard(lanes = SCENARIO_KLCC.lanes)
        }

        // Weather card
        WeatherCard(
            weather  = SCENARIO_KLCC.weather,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp)
                .widthIn(max = 180.dp),
        )

        // Map controls
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MapCtrlButton(Icons.Default.MyLocation, "Re-center")
            MapCtrlButton(Icons.Default.Add,        "Zoom in")
            MapCtrlButton(Icons.Default.Remove,     "Zoom out")
        }

        // Speedometer
        SpeedometerCluster(
            speedKmh = currentSpeedKmh,
            limitKmh = SCENARIO_KLCC.speedLimitKmh,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 148.dp),
        )

        // Bottom control deck
        BottomControlDeck(
            modifier       = Modifier.align(Alignment.BottomCenter),
            arrivalTime    = arrivalTime,
            etaMinutes     = navKey.etaMinutes,
            distanceKm     = navKey.distanceKm,
            hasToll        = navKey.hasToll,
            tollAmountRm   = navKey.tollAmountRm,
            isDriving      = isDriving,
            isBoost        = isBoost,
            onToggleDrive  = {
                isDriving = !isDriving
                if (isDriving) targetSpeedKmh = 95f
            },
            onBoost = {
                isBoost = !isBoost
                targetSpeedKmh = if (isBoost) SCENARIO_KLCC.speedLimitKmh + 30f
                                 else SCENARIO_KLCC.speedLimitKmh - 10f
            },
            onEndNav = onEndNav,
        )
    }
}

// ─── Maneuver HUD ────────────────────────────────────────────────
@Composable
fun ManeuverHud(distanceM: Int) {
    Surface(
        shape           = RoundedCornerShape(18.dp),
        color           = SurfacePanel.copy(alpha = 0.95f),
        shadowElevation = 12.dp,
        modifier        = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier  = Modifier.padding(14.dp),
            verticalAlignment   = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(ApexCyan.copy(alpha = 0.15f), RoundedCornerShape(14.dp))
                    .border(1.dp, ApexCyan.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Default.TurnRight, null, tint = ApexCyan, modifier = Modifier.size(32.dp))
            }

            Column {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        "$distanceM",
                        color      = TextPrimary,
                        fontSize   = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 32.sp,
                    )
                    Text("m", color = ApexCyan, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
                Text(
                    SCENARIO_KLCC.maneuverInstruction,
                    color      = TextPrimary,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 17.sp,
                )
                Text(
                    SCENARIO_KLCC.maneuverSubInstruction,
                    color    = TextSecondary,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }
    }
}

// ─── Lane Guidance Card ──────────────────────────────────────────
@Composable
fun LaneGuidanceCard(lanes: List<LaneInfo>) {
    Surface(
        shape    = RoundedCornerShape(14.dp),
        color    = SurfacePanel.copy(alpha = 0.92f),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(shape = RoundedCornerShape(4.dp), color = ApexCyan.copy(alpha = 0.15f)) {
                    Text(
                        "LANE GUIDANCE",
                        modifier   = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        color      = ApexCyan,
                        fontSize   = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.8.sp,
                    )
                }
                val recommended = lanes.filter { it.isRecommended }.joinToString(" & ") { "${it.id}" }
                Text(
                    "Lane $recommended of ${lanes.size}",
                    color      = ApexEmerald,
                    fontSize   = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                lanes.forEach { lane ->
                    LaneIndicator(lane = lane, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun LaneIndicator(lane: LaneInfo, modifier: Modifier = Modifier) {
    val borderColor = if (lane.isRecommended) ApexEmerald else BorderSubtle
    val bgColor     = if (lane.isRecommended) ApexEmerald.copy(alpha = 0.18f) else SurfaceElevated
    val arrowColor  = if (lane.isRecommended) ApexEmerald else TextMuted

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_${lane.id}")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue  = if (lane.isRecommended) 0.6f else 1f,
        targetValue   = 1f,
        animationSpec = infiniteRepeatable(tween(1000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label         = "laneAlpha_${lane.id}",
    )

    Column(
        modifier = modifier
            .height(52.dp)
            .background(bgColor, RoundedCornerShape(8.dp))
            .border(
                if (lane.isRecommended) 2.dp else 1.dp,
                borderColor,
                RoundedCornerShape(8.dp),
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = when (lane.type) {
                LaneType.EXIT_RIGHT, LaneType.STRAIGHT_RIGHT -> Icons.Default.TurnRight
                LaneType.LEFT_ONLY                           -> Icons.Default.TurnLeft
                else                                         -> Icons.Default.ArrowUpward
            },
            contentDescription = null,
            tint     = arrowColor,
            modifier = Modifier.size(20.dp),
        )
        Text(
            lane.label,
            color      = if (lane.isRecommended) ApexEmerald else TextMuted,
            fontSize   = 7.sp,
            fontWeight = FontWeight.Bold,
            maxLines   = 1,
            textAlign  = TextAlign.Center,
        )
    }
}

// ─── Speed Camera Radar Alert ────────────────────────────────────
@Composable
fun RadarAlertCard(
    limitKmh: Int,
    distanceM: Int,
    progressPct: Float,
    isOverspeed: Boolean,
    currentSpeedKmh: Int,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shake")
    val shake by infiniteTransition.animateFloat(
        initialValue  = -2f,
        targetValue   = 2f,
        animationSpec = infiniteRepeatable(tween(120), RepeatMode.Reverse),
        label         = "shakeAnim",
    )

    Surface(
        shape  = RoundedCornerShape(16.dp),
        color  = Color(0xFF1A090E).copy(alpha = 0.96f),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, ApexCrimson),
        shadowElevation = 20.dp,
        modifier = Modifier
            .width(220.dp)
            .rotate(if (isOverspeed) shake else 0f),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "SPEED CAMERA",
                    color         = ApexCrimson,
                    fontSize      = 11.sp,
                    fontWeight    = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp,
                )
                Surface(shape = RoundedCornerShape(8.dp), color = ApexCrimson) {
                    Text(
                        "$limitKmh",
                        modifier   = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        color      = Color.White,
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )
                }
            }

            Row(
                verticalAlignment      = Alignment.Bottom,
                horizontalArrangement  = Arrangement.spacedBy(4.dp),
            ) {
                Text("$distanceM", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
                Text("m ahead", color = Color(0xFFFF8A80), fontSize = 12.sp)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(BorderSubtle)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progressPct.coerceIn(0f, 1f))
                        .fillMaxHeight()
                        .background(Brush.horizontalGradient(listOf(ApexCrimson, Color(0xFFFF8A80))))
                )
            }

            Text(
                if (isOverspeed) "⚠️ OVERSPEED! Slow down now!"
                else "✅ Speed OK ($currentSpeedKmh km/h)",
                color      = if (isOverspeed) ApexCrimson else ApexEmerald,
                fontSize   = 10.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

// ─── Weather Card ────────────────────────────────────────────────
@Composable
fun WeatherCard(weather: WeatherInfo, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape    = RoundedCornerShape(14.dp),
        color    = SurfacePanel.copy(alpha = 0.92f),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Box(modifier = Modifier.size(7.dp).background(ApexCyan, CircleShape))
                Text("WEATHER", color = ApexCyan, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            }
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(weather.emoji, fontSize = 24.sp)
                Text("${weather.tempC}°C", color = TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            }
            Text(weather.condition, color = TextSecondary, fontSize = 10.sp)
            Text(
                weather.surface,
                color      = if (weather.surface.contains("Wet") || weather.surface.contains("Icy"))
                    ApexAmber else ApexEmerald,
                fontSize   = 10.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

// ─── Speedometer ─────────────────────────────────────────────────
@Composable
fun SpeedometerCluster(speedKmh: Float, limitKmh: Int, modifier: Modifier = Modifier) {
    val isOver     = speedKmh > limitKmh
    val gaugeColor = if (isOver) ApexCrimson else ApexCyan
    val sweepAngle = (speedKmh / 160f).coerceIn(0f, 1f) * 270f

    Surface(
        modifier        = modifier,
        shape           = RoundedCornerShape(20.dp),
        color           = SurfacePanel.copy(alpha = 0.95f),
        shadowElevation = 10.dp,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Circular arc gauge
            Box(modifier = Modifier.size(90.dp), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val stroke = 10f
                    drawArc(
                        color      = BorderSubtle,
                        startAngle = 135f, sweepAngle = 270f,
                        useCenter  = false,
                        style      = Stroke(stroke, cap = StrokeCap.Round),
                    )
                    drawArc(
                        color      = gaugeColor,
                        startAngle = 135f, sweepAngle = sweepAngle,
                        useCenter  = false,
                        style      = Stroke(stroke + 2f, cap = StrokeCap.Round),
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${speedKmh.toInt()}", color = TextPrimary, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 26.sp)
                    Text("km/h", color = TextMuted, fontSize = 8.sp, letterSpacing = 0.5.sp)
                }
            }

            // Speed limit sign + delta
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color.White, CircleShape)
                        .border(6.dp, Color(0xFFE53935), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("LIMIT", color = Color(0xFF37474F), fontSize = 7.sp, fontWeight = FontWeight.ExtraBold)
                        Text("$limitKmh", color = Color(0xFF111111), fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 22.sp)
                    }
                }

                val delta = speedKmh.toInt() - limitKmh
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isOver) ApexCrimson.copy(0.18f) else ApexEmerald.copy(0.12f),
                ) {
                    Text(
                        if (isOver) "+$delta OVER" else "${kotlin.math.abs(delta)} under",
                        modifier   = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        color      = if (isOver) ApexCrimson else ApexEmerald,
                        fontSize   = 9.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

// ─── Bottom Control Deck ─────────────────────────────────────────
@Composable
fun BottomControlDeck(
    modifier: Modifier,
    arrivalTime: String,
    etaMinutes: Int,
    distanceKm: Double,
    hasToll: Boolean,
    tollAmountRm: Double,
    isDriving: Boolean,
    isBoost: Boolean,
    onToggleDrive: () -> Unit,
    onBoost: () -> Unit,
    onEndNav: () -> Unit,
) {
    Surface(
        modifier        = modifier.fillMaxWidth(),
        shape           = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color           = SurfacePanel.copy(alpha = 0.97f),
        shadowElevation = 24.dp,
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            // ETA row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Column {
                    Text(arrivalTime, color = ApexEmerald, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 32.sp)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment     = Alignment.CenterVertically,
                    ) {
                        Text("$etaMinutes min", color = TextPrimary,   fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("•",               color = TextMuted,     fontSize = 13.sp)
                        Text("$distanceKm km",  color = TextSecondary, fontSize = 13.sp)
                        if (hasToll) {
                            Text("•",  color = TextMuted, fontSize = 13.sp)
                            Text("🚧 RM %.2f".format(tollAmountRm), color = ApexAmber, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                // Traffic mini bar
                Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("TRAFFIC", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Row(
                        modifier = Modifier.width(100.dp).height(7.dp).clip(RoundedCornerShape(4.dp)),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        Box(Modifier.weight(3f).fillMaxHeight().background(ApexEmerald))
                        Box(Modifier.weight(2f).fillMaxHeight().background(ApexAmber))
                        Box(Modifier.weight(1f).fillMaxHeight().background(ApexCrimson))
                        Box(Modifier.weight(4f).fillMaxHeight().background(ApexEmerald))
                    }
                    Text("Live traffic active", color = TextMuted, fontSize = 9.sp)
                }
            }

            Spacer(Modifier.height(12.dp))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Button(
                    onClick  = onToggleDrive,
                    modifier = Modifier.weight(2f),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor = if (isDriving) ApexAmber else ApexCyan,
                        contentColor   = SurfaceDark,
                    ),
                ) {
                    Icon(if (isDriving) Icons.Default.Pause else Icons.Default.PlayArrow, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(if (isDriving) "Pause" else "Start Drive", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                }

                OutlinedButton(
                    onClick  = onBoost,
                    modifier = Modifier.weight(1.5f),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = ButtonDefaults.outlinedButtonColors(
                        contentColor   = if (isBoost) ApexCrimson else TextSecondary,
                        containerColor = if (isBoost) ApexCrimson.copy(alpha = 0.12f) else Color.Transparent,
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isBoost) ApexCrimson else BorderSubtle),
                ) {
                    Icon(Icons.Default.Speed, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Boost", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick  = onEndNav,
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = ButtonDefaults.outlinedButtonColors(contentColor = ApexCrimson),
                    border   = androidx.compose.foundation.BorderStroke(1.dp, ApexCrimson.copy(alpha = 0.5f)),
                ) {
                    Icon(Icons.Default.Close, null, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
