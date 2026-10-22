package com.example.apexnav

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.apexnav.ui.map.MapHomeScreen
import com.example.apexnav.ui.navigation.ActiveNavScreen
import com.example.apexnav.ui.splash.SplashScreen

@Composable
fun MainNavigation() {
    val backStack = rememberNavBackStack(Splash)

    NavDisplay(
        backStack = backStack,
        onBack    = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Splash> {
                SplashScreen(
                    onAnimationEnd = {
                        backStack.removeLastOrNull()
                        backStack.add(MapHome)
                    }
                )
            }
            entry<MapHome> {
                MapHomeScreen(
                    onNavigate = { key: NavKey -> backStack.add(key) }
                )
            }
            entry<ActiveNav> { key ->
                ActiveNavScreen(
                    navKey   = key,
                    onEndNav = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}
