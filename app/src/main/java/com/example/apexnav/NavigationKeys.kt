package com.example.apexnav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object Splash : NavKey
@Serializable data object MapHome : NavKey
@Serializable data class ActiveNav(
    val destinationName: String,
    val routeId: Int,
    val etaMinutes: Int,
    val distanceKm: Double,
    val hasToll: Boolean,
    val tollAmountRm: Double,
) : NavKey
