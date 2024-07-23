package dev.maxim_v.weather_app.presentation.navigation

sealed class Screen(val route: String) {
    data object MainScreen: Screen(ROUTE_MAIN_SCREEN)
    data object SettingsScreen: Screen(ROUTE_SETTINGS_SCREEN)

    private companion object {
        const val ROUTE_MAIN_SCREEN = "main_screen"
        const val ROUTE_SETTINGS_SCREEN = "settings_screen"
    }
}