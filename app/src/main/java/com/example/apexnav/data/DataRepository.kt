package com.example.apexnav.data

// ─── Favourite Place ────────────────────────────────────────────
data class FavouritePlace(
    val id: String,
    val name: String,
    val subtitle: String,
    val emoji: String,
    val etaMinutes: Int?,
    val lat: Double,
    val lng: Double,
)

val KL_FAVOURITES = listOf(
    FavouritePlace("klcc",    "KLCC Twin Towers",   "Kuala Lumpur City Centre",       "🏙️", 14, 3.1478, 101.6953),
    FavouritePlace("sunway",  "Sunway Pyramid",     "Petaling Jaya, Selangor",        "🛍️", 22, 3.0738, 101.5183),
    FavouritePlace("putraj",  "Putrajaya",          "Federal Administrative Capital", "🏛️", 38, 2.9264, 101.6464),
    FavouritePlace("shell",   "Shell Jalan Ampang", "Petrol Station • Open 24h",      "⛽",  8, 3.2159, 101.7105),
    FavouritePlace("home",    "Home",               "Jalan Ampang, KL",               "🏠", null, 3.1466, 101.7075),
)

// ─── Route Option ───────────────────────────────────────────────
enum class TrafficLevel { LIGHT, MODERATE, HEAVY }

data class TrafficSegment(val color: SegmentColor, val weight: Float)
enum class SegmentColor { GREEN, YELLOW, RED }

data class RouteOption(
    val id: Int,
    val label: String,
    val via: String,
    val etaMinutes: Int,
    val distanceKm: Double,
    val hasToll: Boolean,
    val tollAmountRm: Double,
    val trafficLevel: TrafficLevel,
    val trafficSegments: List<TrafficSegment>,
    val badge: String,
    val isRecommended: Boolean = false,
)

fun routesForDestination(destinationName: String): List<RouteOption> = listOf(
    RouteOption(
        id = 0, label = "Fastest Route",
        via = "DUKE Highway → Jln P. Ramlee",
        etaMinutes = 14, distanceKm = 8.2,
        hasToll = true, tollAmountRm = 2.50,
        trafficLevel = TrafficLevel.MODERATE,
        trafficSegments = listOf(
            TrafficSegment(SegmentColor.GREEN, 4f),
            TrafficSegment(SegmentColor.YELLOW, 2f),
            TrafficSegment(SegmentColor.GREEN, 3f),
        ),
        badge = "RECOMMENDED", isRecommended = true,
    ),
    RouteOption(
        id = 1, label = "Toll-Free Route",
        via = "Jln Ampang → Jln Tun Razak",
        etaMinutes = 22, distanceKm = 10.5,
        hasToll = false, tollAmountRm = 0.0,
        trafficLevel = TrafficLevel.HEAVY,
        trafficSegments = listOf(
            TrafficSegment(SegmentColor.GREEN, 2f),
            TrafficSegment(SegmentColor.YELLOW, 3f),
            TrafficSegment(SegmentColor.RED, 2f),
            TrafficSegment(SegmentColor.GREEN, 1f),
        ),
        badge = "TOLL FREE",
    ),
    RouteOption(
        id = 2, label = "Scenic Route",
        via = "Jln Kuching → Mahameru Hwy",
        etaMinutes = 29, distanceKm = 14.8,
        hasToll = true, tollAmountRm = 1.10,
        trafficLevel = TrafficLevel.LIGHT,
        trafficSegments = listOf(
            TrafficSegment(SegmentColor.GREEN, 6f),
            TrafficSegment(SegmentColor.YELLOW, 1f),
            TrafficSegment(SegmentColor.GREEN, 2f),
        ),
        badge = "LESS TRAFFIC",
    ),
)

// ─── Lane Info ──────────────────────────────────────────────────
enum class LaneType { STRAIGHT, STRAIGHT_RIGHT, EXIT_RIGHT, LEFT_ONLY, SLOW }

data class LaneInfo(
    val id: Int,
    val label: String,
    val type: LaneType,
    val isRecommended: Boolean,
)

// ─── Navigation Scenario ────────────────────────────────────────
data class WeatherInfo(
    val location: String,
    val tempC: Int,
    val emoji: String,
    val condition: String,
    val surface: String,
    val visibilityKm: Double,
)

data class SpeedCamera(
    val type: String,
    val limitKmh: Int,
    val lat: Double,
    val lng: Double,
    val triggerDistanceM: Int,
)

data class NavigationScenario(
    val name: String,
    val centerLat: Double,
    val centerLng: Double,
    val zoom: Double,
    val pitch: Double,
    val bearing: Double,
    val speedLimitKmh: Int,
    val weather: WeatherInfo,
    val lanes: List<LaneInfo>,
    val camera: SpeedCamera,
    val maneuverInstruction: String,
    val maneuverSubInstruction: String,
    val maneuverDistanceM: Int,
    val pathPoints: List<Triple<Double, Double, Int>>, // lat, lng, speed
)

val SCENARIO_KLCC = NavigationScenario(
    name = "KLCC via DUKE Highway",
    centerLat = 3.1478, centerLng = 101.6953,
    zoom = 16.0, pitch = 60.0, bearing = 200.0,
    speedLimitKmh = 110,
    weather = WeatherInfo("Kuala Lumpur", 31, "⛅", "Partly Cloudy", "Dry / Optimal", 12.0),
    lanes = listOf(
        LaneInfo(1, "Lane 1", LaneType.STRAIGHT,       false),
        LaneInfo(2, "Lane 2", LaneType.STRAIGHT,       false),
        LaneInfo(3, "Lane 3", LaneType.STRAIGHT,       true),
        LaneInfo(4, "Exit KLCC", LaneType.EXIT_RIGHT,  true),
    ),
    camera = SpeedCamera("JPJ ANDES Camera", 110, 3.1530, 101.7023, 500),
    maneuverInstruction = "Keep right towards KLCC via Jln P. Ramlee",
    maneuverSubInstruction = "Take exit for KLCC in 800m",
    maneuverDistanceM = 800,
    pathPoints = listOf(
        Triple(3.1478, 101.6953, 95),
        Triple(3.1495, 101.6970, 100),
        Triple(3.1510, 101.6990, 105),
        Triple(3.1520, 101.7010, 108),
        Triple(3.1530, 101.7023, 98),
        Triple(3.1540, 101.7035, 90),
        Triple(3.1550, 101.7050, 85),
    ),
)

// ─── Travel Mode ────────────────────────────────────────────────
enum class TravelMode(val label: String, val emoji: String) {
    DRIVE("Drive", "🚗"),
    WALK("Walk", "🚶"),
    TRANSIT("Transit", "🚌"),
}
