package dev.maxim_v.weather_app.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.maxim_v.weather_app.presentation.weather.LocationSearchScreenRoot
import dev.maxim_v.weather_app.presentation.weather.MainScreenRoot
import dev.maxim_v.weather_app.presentation.weather.SettingsScreenRoot

private const val FADE_TRANSITION_TIME = 700
private const val SLIDE_TRANSITION_TIME = 500
private const val NEED_REFRESH = "refresh"

@Composable
fun AppNavGraph(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.MainScreen.route,
        enterTransition = { fadeIn(animationSpec = tween(FADE_TRANSITION_TIME)) },
        exitTransition = { fadeOut(animationSpec = tween(FADE_TRANSITION_TIME)) },
        popEnterTransition = { fadeIn(animationSpec = tween(FADE_TRANSITION_TIME)) },
        popExitTransition = { fadeOut(animationSpec = tween(FADE_TRANSITION_TIME)) }
    ) {
        composable(Screen.MainScreen.route) {
            val needToRefresh = it.savedStateHandle.get<Boolean>(NEED_REFRESH)

            MainScreenRoot(
                onSettingIconClick = { navHostController.navigate(Screen.SettingsScreen.route) },
                onSearchIconClick = { navHostController.navigate(Screen.LocationSearchScreen.route) },
                needRefresh = needToRefresh
            )
        }
        settingsScreen {
            SettingsScreenRoot(
                onBackIconClick = {
                    navHostController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(NEED_REFRESH, it)
                    navHostController.popBackStack()
                },
                onBackPress = {
                    navHostController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(NEED_REFRESH, it)
                    navHostController.popBackStack()
                })
        }
        locationSearchScreen {
            LocationSearchScreenRoot(
                onBackIconClick = { navHostController.popBackStack() },
                onLocationSelected = {
                    navHostController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(NEED_REFRESH, it)
                    navHostController.popBackStack()
                }
            )
        }
    }
}

fun NavGraphBuilder.locationSearchScreen(locationSearchScreen: @Composable () -> Unit) {
    composable(
        route = Screen.LocationSearchScreen.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(SLIDE_TRANSITION_TIME)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(SLIDE_TRANSITION_TIME)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(SLIDE_TRANSITION_TIME)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(SLIDE_TRANSITION_TIME)
            )
        }) {
        locationSearchScreen()
    }
}

fun NavGraphBuilder.settingsScreen(settingsScreen: @Composable () -> Unit) {
    composable(
        route = Screen.SettingsScreen.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(SLIDE_TRANSITION_TIME)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(SLIDE_TRANSITION_TIME)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(SLIDE_TRANSITION_TIME)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(SLIDE_TRANSITION_TIME)
            )
        }) {
        settingsScreen()
    }
}