package com.example.apexnav.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.example.apexnav.ActiveNav
import com.example.apexnav.data.*
import com.example.apexnav.theme.*
import com.example.apexnav.ui.components.MapCtrlButton

@Composable
fun MapHomeScreen(onNavigate: (NavKey) -> Unit) {
    var selectedMode    by remember { mutableStateOf(TravelMode.DRIVE) }
    var searchQuery     by remember { mutableStateOf("") }
    var showRoutePicker by remember { mutableStateOf(false) }
    var selectedDest    by remember { mutableStateOf<FavouritePlace?>(null) }
    var selectedRoute   by remember { mutableStateOf<RouteOption?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark)
    ) {
        // ── Map placeholder ───────────────────────────────────────
        MapPlaceholder()

        // ── Top bar (search + mode) ───────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp)
                .padding(top = 52.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                // ApexNav logo badge
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            Brush.linearGradient(listOf(ApexCyan, ApexBlue)),
                            CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("⟡", fontSize = 18.sp, color = SurfaceDark)
                }

                // Search field
                Surface(
                    modifier       = Modifier.weight(1f),
                    shape          = RoundedCornerShape(14.dp),
                    color          = SurfacePanel.copy(alpha = 0.95f),
                    tonalElevation = 4.dp,
                ) {
                    TextField(
                        value         = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder   = { Text("Search in Malaysia…", color = TextMuted) },
                        leadingIcon   = { Icon(Icons.Default.Search, null, tint = TextMuted) },
                        singleLine    = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor   = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor   = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor        = TextPrimary,
                            unfocusedTextColor      = TextPrimary,
                            cursorColor             = ApexCyan,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            // Mode switcher
            ModeSwitcherRow(
                selectedMode = selectedMode,
                onModeChange = { selectedMode = it },
            )
        }

        // ── Map controls (right side) ─────────────────────────────
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement   = Arrangement.spacedBy(8.dp),
            horizontalAlignment   = Alignment.CenterHorizontally,
        ) {
            MapCtrlButton(Icons.Default.MyLocation, "Re-center")
            MapCtrlButton(Icons.Default.Add,        "Zoom in")
            MapCtrlButton(Icons.Default.Remove,     "Zoom out")
            MapCtrlButton(Icons.Default.Layers,     "3D / 2D")
        }

        // ── Favourites + bottom anchor ────────────────────────────
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 24.dp),
        ) {
            FavouritesStrip(
                places   = KL_FAVOURITES,
                onSelect = { place ->
                    selectedDest  = place
                    selectedRoute = routesForDestination(place.name).first()
                    showRoutePicker = true
                },
            )
        }

        // ── Route picker bottom sheet ─────────────────────────────
        if (showRoutePicker && selectedDest != null) {
            RoutePickerSheet(
                destination   = selectedDest!!,
                routes        = routesForDestination(selectedDest!!.name),
                selectedRoute = selectedRoute,
                onRouteSelect = { selectedRoute = it },
                onStartNav    = { route ->
                    showRoutePicker = false
                    onNavigate(
                        ActiveNav(
                            destinationName = selectedDest!!.name,
                            routeId         = route.id,
                            etaMinutes      = route.etaMinutes,
                            distanceKm      = route.distanceKm,
                            hasToll         = route.hasToll,
                            tollAmountRm    = route.tollAmountRm,
                        )
                    )
                },
                onDismiss = { showRoutePicker = false },
            )
        }
    }
}

// ─── Map Placeholder ─────────────────────────────────────────────
@Composable
fun MapPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF0F1924), Color(0xFF07090E)),
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            "🗺️  Map Loading…\nKuala Lumpur, Malaysia",
            color     = TextMuted.copy(alpha = 0.4f),
            fontSize  = 13.sp,
            lineHeight = 22.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
    }
}

// ─── Mode Switcher ───────────────────────────────────────────────
@Composable
fun ModeSwitcherRow(
    selectedMode: TravelMode,
    onModeChange: (TravelMode) -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = SurfacePanel.copy(alpha = 0.92f),
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            TravelMode.entries.forEach { mode ->
                val selected = mode == selectedMode
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onModeChange(mode) },
                    shape = RoundedCornerShape(10.dp),
                    color = if (selected) ApexCyan.copy(alpha = 0.15f) else Color.Transparent,
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment   = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(mode.emoji, fontSize = 14.sp)
                        Text(
                            mode.label,
                            fontSize   = 13.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            color      = if (selected) ApexCyan else TextSecondary,
                        )
                    }
                }
            }
        }
    }
}

// ─── Favourites Strip ────────────────────────────────────────────
@Composable
fun FavouritesStrip(
    places: List<FavouritePlace>,
    onSelect: (FavouritePlace) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text("★", color = ApexAmber, fontSize = 12.sp)
            Text(
                "FAVOURITES",
                color         = TextSecondary,
                fontSize      = 10.sp,
                fontWeight    = FontWeight.Bold,
                letterSpacing = 1.sp,
            )
        }

        LazyRow(
            contentPadding        = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(places) { place ->
                FavouriteCard(place = place, onClick = { onSelect(place) })
            }
        }
    }
}

@Composable
fun FavouriteCard(place: FavouritePlace, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .width(148.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape           = RoundedCornerShape(16.dp),
        color           = SurfacePanel.copy(alpha = 0.95f),
        shadowElevation = 8.dp,
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(place.emoji, fontSize = 20.sp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    place.name,
                    color      = TextPrimary,
                    fontSize   = 11.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis,
                )
                if (place.etaMinutes != null) {
                    Text(
                        "${place.etaMinutes} min",
                        color      = ApexEmerald,
                        fontSize   = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                } else {
                    Text("Set location", color = TextMuted, fontSize = 10.sp)
                }
            }
        }
    }
}
