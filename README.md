# ApexNav 🚀

[![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.3-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Navigation 3](https://img.shields.io/badge/AndroidX-Navigation%203-34A853?style=for-the-badge)](https://developer.android.com/guide/navigation)
[![OpenStreetMap](https://img.shields.io/badge/Map%20Engine-osmdroid-7EBC6F?style=for-the-badge&logo=openstreetmap&logoColor=white)](https://osmdroid.github.io/osmdroid/)

**ApexNav** is a futuristic, next-generation turn-by-turn navigation application built for Android. Engineered with **Jetpack Compose**, **Material 3**, and **AndroidX Navigation 3**, ApexNav pairs an interactive dark-themed **OpenStreetMap** engine with an immersive heads-up driving display (HUD), real-time lane guidance, speed camera enforcement alerts, multi-route selection, and road weather telemetry.

---

## 🌟 Key Features

- **🗺️ High-Contrast Interactive Map Engine**  
  Powered by `osmdroid` (OpenStreetMap) with hardware-accelerated tile rendering, multi-touch gestures, custom dark-navigation color matrix filter, interactive compass, zoom controls, and a pulsing vehicle position puck.

- **🧭 Turn-by-Turn Navigation HUD**  
  Immersive in-trip dashboard featuring real-time directional maneuvers, distance indicators, route completion progress, dynamic vehicle speed tracking, and automated trip simulation.

- **🛣️ Active Lane Guidance**  
  Multi-lane visualization advising drivers on lane positioning and highlighting optimal lanes for upcoming freeway splits and exits.

- **🚦 Smart Multi-Route Selection**  
  Instant route comparisons with intelligent badges (*Fastest*, *Toll-Free*, *Scenic*), estimated travel times (ETA), exact distances, toll costs (MYR / RM), and segmented live traffic congestion heatmaps (Light, Moderate, Heavy).

- **📷 Safety & Speed Camera Alerts**  
  Configurable proximity warnings for speed enforcement cameras (e.g., JPJ / automated traffic cameras) and real-time speed limit indicators.

- **🌦️ Environmental & Surface Telemetry**  
  Live atmospheric condition reports (weather icon, temperature, visibility range) and road surface state tracking (Dry / Optimal vs. Wet).

- **⚡ Quick-Access Bookmarks & Travel Modes**  
  Fast one-tap routing for favorite destinations and flexible travel mode switching (**Drive** 🚗, **Walk** 🚶, **Transit** 🚌).

---

## 🛠️ Architecture & Tech Stack

ApexNav is built following modern Android development best practices and Clean Architecture principles:

| Layer / Component | Technology / Library | Description |
| :--- | :--- | :--- |
| **Language** | Kotlin 2.3+ | 100% Kotlin with JVM Toolchain target 17 |
| **UI Framework** | Jetpack Compose (BOM 2026.03.01) | Declarative UI with Material 3 components |
| **Navigation** | AndroidX Navigation 3 (`nav3`) | Type-safe `@Serializable` `NavKey` routing and backstack provider |
| **Mapping Engine** | `osmdroid-android` (6.1.20) | Offline-capable OpenStreetMap viewer with custom color matrix filtering |
| **Asynchronous** | Kotlinx Coroutines & Flow | Asynchronous task handling, event streams, and reactive state management |
| **Serialization** | Kotlinx Serialization | Type-safe route arguments and serialization |
| **Build System** | Gradle Kotlin DSL (`build.gradle.kts`) | Version catalog (`libs.versions.toml`) & Android Gradle Plugin 9.x |

---

## 📁 Project Structure

```plaintext
ApexNav/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml          # App permissions and activity declarations
│   │   │   ├── java/com/example/apexnav/
│   │   │   │   ├── MainActivity.kt          # Main Activity entry point
│   │   │   │   ├── Navigation.kt            # Navigation3 NavDisplay container
│   │   │   │   ├── NavigationKeys.kt        # Type-safe navigation keys (Splash, MapHome, ActiveNav)
│   │   │   │   ├── data/
│   │   │   │   │   └── DataRepository.kt    # Models, routes, favourites, and navigation scenarios
│   │   │   │   ├── theme/                   # Color palettes, typography, and Material 3 theme
│   │   │   │   │   ├── Color.kt
│   │   │   │   │   ├── Theme.kt
│   │   │   │   │   └── Type.kt
│   │   │   │   └── ui/
│   │   │   │       ├── components/          # Reusable UI (InteractiveMapView, MapCtrlButton)
│   │   │   │       ├── map/                 # MapHomeScreen & RoutePickerSheet
│   │   │   │       ├── navigation/          # ActiveNavScreen (Turn-by-turn driving HUD)
│   │   │   │       └── splash/              # Animated launch splash screen
│   │   │   └── res/                         # Vector drawables, mipmaps, strings, and styles
│   │   └── test/                            # Unit and instrumentation tests
│   └── build.gradle.kts                     # App module configuration & dependencies
├── gradle/
│   └── libs.versions.toml                   # Centralized version catalog
├── build.gradle.kts                         # Root build file
├── settings.gradle.kts                      # Module inclusion and repository settings
└── README.md                                # Project documentation
```

---
<img src="https://github.com/user-attachments/assets/8a6a0e90-75a2-4c9d-93cc-bdb29cde1a5f" width="300" alt="Banner">
<img src="https://github.com/user-attachments/assets/821c8e32-0703-435c-995f-c933044bcd2d" width="300" alt="Banner">
<img src="https://github.com/user-attachments/assets/85e6e2c3-124a-4a74-bb14-70d3fc0e132b" width="300" alt="Banner">
---

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio**: Android Studio Ladybug / Meerkat (or newer)
- **JDK**: Java Development Kit 17 (recommended: OpenJDK 17 or Android Studio Embedded JDK)
- **Android SDK**:
  - `compileSdk`: 36
  - `minSdk`: 24 (Android 7.0 Nougat+)
  - `targetSdk`: 36

### Cloning the Repository

```bash
git clone https://github.com/LynxPlayz24/ApexNav.git
cd ApexNav
```

### Building & Running

1. Open the project in **Android Studio**.
2. Allow Gradle sync to complete and download dependencies from Google Maven and Maven Central.
3. Connect an Android device (via USB / Wireless ADB) or launch an Android Virtual Device (AVD).
4. Run the application:
   - Click the **Run** button (green triangle) in Android Studio, or execute via terminal:

```bash
# Debug APK Build
./gradlew assembleDebug

# Install on connected device/emulator
./gradlew installDebug
```

---

## 🔒 Permissions

ApexNav requests the following runtime/manifest permissions to provide navigation services:

- `android.permission.INTERNET` — To download OpenStreetMap tiles and routing information.
- `android.permission.ACCESS_NETWORK_STATE` — To monitor online/offline connectivity.
- `android.permission.ACCESS_FINE_LOCATION` — For high-precision GPS positioning during navigation.
- `android.permission.ACCESS_COARSE_LOCATION` — For approximate location determination.

---

## 🎨 Design Philosophy & Theme

ApexNav features a custom cyber-inspired dark navigation theme optimized for nighttime driving and OLED displays:

- **Apex Cyan (`#00F2FE`)** & **Apex Blue (`#4FACFE`)**: Primary accent and trajectory highlights.
- **Apex Emerald (`#00E676`)**: Clear traffic and recommended lane guides.
- **Apex Amber (`#FFAB00`)** & **Apex Crimson (`#FF1744`)**: Congestion indicators and speed camera warnings.
- **Deep Surface Dark (`#07090E`)**: High contrast background for minimal driver glare.

---

## 🤝 Contributing

Contributions, feature suggestions, and bug reports are welcome!

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is open-source and available under the standard MIT License. See [LICENSE](LICENSE) for details if applicable.
