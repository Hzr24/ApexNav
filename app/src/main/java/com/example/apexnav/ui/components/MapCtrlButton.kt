package com.example.apexnav.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.apexnav.theme.SurfacePanel
import com.example.apexnav.theme.TextSecondary

@Composable
fun MapCtrlButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape           = RoundedCornerShape(12.dp),
        color           = SurfacePanel.copy(alpha = 0.92f),
        shadowElevation = 6.dp,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector        = icon,
                contentDescription = contentDescription,
                tint               = TextSecondary,
                modifier           = Modifier.size(20.dp),
            )
        }
    }
}
