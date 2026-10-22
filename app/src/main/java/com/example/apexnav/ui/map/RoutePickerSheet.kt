package com.example.apexnav.ui.map

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apexnav.data.*
import com.example.apexnav.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutePickerSheet(
    destination:   FavouritePlace,
    routes:        List<RouteOption>,
    selectedRoute: RouteOption?,
    onRouteSelect: (RouteOption) -> Unit,
    onStartNav:    (RouteOption) -> Unit,
    onDismiss:     () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest  = onDismiss,
        sheetState        = sheetState,
        containerColor    = SurfacePanel,
        dragHandle        = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 4.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(BorderSubtle)
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        "Choose Your Route",
                        color      = TextPrimary,
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )
                    Text(
                        "to ${destination.name}",
                        color    = TextSecondary,
                        fontSize = 13.sp,
                    )
                }
                Text(destination.emoji, fontSize = 28.sp)
            }

            // Route cards
            routes.forEach { route ->
                RouteCard(
                    route      = route,
                    isSelected = route.id == selectedRoute?.id,
                    onClick    = { onRouteSelect(route) }
                )
            }

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick  = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                ) {
                    Text("Cancel", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick  = { selectedRoute?.let { onStartNav(it) } },
                    enabled  = selectedRoute != null,
                    modifier = Modifier.weight(2f),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor = ApexCyan,
                        contentColor   = SurfaceDark,
                    ),
                ) {
                    Icon(Icons.Default.Navigation, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Start Navigation", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}

// ─── Single Route Card ───────────────────────────────────────────
@Composable
fun RouteCard(
    route:      RouteOption,
    isSelected: Boolean,
    onClick:    () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape  = RoundedCornerShape(16.dp),
        color  = if (isSelected) SurfaceCard else SurfaceElevated,
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(1.5.dp, ApexCyan)
        } else {
            androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
        },
        shadowElevation = if (isSelected) 12.dp else 2.dp,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column {
                    Text(
                        route.label,
                        color      = TextPrimary,
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        "via ${route.via}",
                        color    = TextMuted,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 2.dp),
                    )
                }
                RouteBadge(text = route.badge, isRecommended = route.isRecommended)
            }

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                RouteStat(
                    value    = "${route.etaMinutes} min",
                    label    = "ETA",
                    valueColor = TextPrimary,
                )
                RouteStat(
                    value    = "${route.distanceKm} km",
                    label    = "Distance",
                    valueColor = TextPrimary,
                )
                RouteStat(
                    value    = if (route.hasToll) "RM %.2f".format(route.tollAmountRm) else "No Toll",
                    label    = if (route.hasToll) "🚧 Toll" else "✅ Toll",
                    valueColor = if (route.hasToll) ApexAmber else ApexEmerald,
                )
            }

            // Traffic bar
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Traffic:", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                TrafficBar(segments = route.trafficSegments)
            }
        }
    }
}

@Composable
fun RouteBadge(text: String, isRecommended: Boolean) {
    val bg    = if (isRecommended) ApexEmerald.copy(alpha = 0.18f) else ApexCyan.copy(alpha = 0.12f)
    val color = if (isRecommended) ApexEmerald else ApexCyan

    Surface(
        shape = RoundedCornerShape(6.dp),
        color = bg,
    ) {
        Text(
            text,
            modifier   = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color      = color,
            fontSize   = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.5.sp,
        )
    }
}

@Composable
fun RouteStat(value: String, label: String, valueColor: Color) {
    Column {
        Text(value, color = valueColor, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
        Text(label, color = TextMuted,  fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun TrafficBar(segments: List<TrafficSegment>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(7.dp)
            .clip(RoundedCornerShape(4.dp)),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        val total = segments.sumOf { it.weight.toDouble() }.toFloat()
        segments.forEach { seg ->
            val color = when (seg.color) {
                SegmentColor.GREEN  -> ApexEmerald
                SegmentColor.YELLOW -> ApexAmber
                SegmentColor.RED    -> ApexCrimson
            }
            Box(
                modifier = Modifier
                    .weight(seg.weight / total)
                    .fillMaxHeight()
                    .background(color)
            )
        }
    }
}
