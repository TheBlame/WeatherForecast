package dev.maxim_v.weather_app.presentation.navigation

sealed class Screen(val route: String) {
    data object ForecastScreen: Screen(ROUTE_FORECAST_SCREEN)
    data object SettingsScreen: Screen(ROUTE_SETTINGS_SCREEN)
    data object LocationSearchScreen: Screen(ROUTE_SEARCH_SCREEN)

    private companion object {
        const val ROUTE_FORECAST_SCREEN = "main_screen"
        const val ROUTE_SETTINGS_SCREEN = "settings_screen"
        const val ROUTE_SEARCH_SCREEN = "search_screen"
    }
}