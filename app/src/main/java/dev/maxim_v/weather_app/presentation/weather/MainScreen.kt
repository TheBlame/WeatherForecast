package dev.maxim_v.weather_app.presentation.weather

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.maxim_v.weather_app.R
import dev.maxim_v.weather_app.domain.entity.WeatherModel.CurrentSample
import dev.maxim_v.weather_app.domain.entity.WeatherModel.DailySample
import dev.maxim_v.weather_app.domain.entity.WeatherModel.HourlySample
import dev.maxim_v.weather_app.domain.entity.WeatherSample.CURRENT
import dev.maxim_v.weather_app.domain.entity.WeatherSample.DAILY
import dev.maxim_v.weather_app.domain.entity.WeatherSample.HOURLY
import dev.maxim_v.weather_app.presentation.ui.theme.ReplacementTheme
import dev.maxim_v.weather_app.presentation.ui.theme.WeatherForecastTheme
import dev.maxim_v.weather_app.presentation.viewmodels.MainScreenEvent
import dev.maxim_v.weather_app.presentation.viewmodels.MainScreenState
import dev.maxim_v.weather_app.presentation.viewmodels.MainScreenViewModel
import dev.maxim_v.weather_app.util.checkLocationPermissions
import kotlinx.coroutines.launch

@Composable
fun MainScreenRoot() {
    val mainScreenViewModel: MainScreenViewModel = viewModel()
    MainScreen(
        screenState = mainScreenViewModel.mainScreenState,
        onEvent = mainScreenViewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(screenState: MainScreenState, onEvent: (MainScreenEvent) -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val locationPermissionRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                scope.launch {
                    onEvent(MainScreenEvent.GetLocationWithGps)
                }
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                scope.launch {
                    onEvent(MainScreenEvent.GetLocationWithGps)
                }
            }

            else -> {
                Log.d("PERMISION", "DENIED")
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.location_permissions),
                        actionLabel = "",
                        duration = SnackbarDuration.Indefinite,
                        withDismissAction = true
                    )
                }
            }
        }
    }

    WeatherForecastTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Тюмень",
                            style = ReplacementTheme.typography.large
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = null
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            if (checkLocationPermissions(context)
                            ) {
                                scope.launch {
                                    onEvent(MainScreenEvent.GetLocationWithGps)
                                }
                            } else {
                                locationPermissionRequest.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            }

                        }) {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = null
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null
                            )
                        }
                    }
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) { innerPadding ->
            when (screenState) {
                is MainScreenState.Success -> {
                    MainScreenContent(
                        modifier = Modifier
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState()),
                        currentSample = screenState.data[CURRENT] as CurrentSample,
                        hourlySample = screenState.data[HOURLY] as HourlySample,
                        dailySample = screenState.data[DAILY] as DailySample
                    )
                }

                MainScreenState.Error -> {}
                MainScreenState.Initial -> {}
                MainScreenState.Loading -> {}
            }
        }
    }
}

@Composable
private fun MainScreenContent(
    modifier: Modifier = Modifier,
    currentSample: CurrentSample,
    hourlySample: HourlySample,
    dailySample: DailySample
) {
    Column(
        modifier = modifier
    ) {
        CurrentWeatherCard(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            currentWeather = currentSample
        )
        HourlyChart(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer
                )
                .horizontalScroll(rememberScrollState())
                .height(240.dp)
                .width(1800.dp),
            data = hourlySample.data,
            mainGraphLineColor = MaterialTheme.colorScheme.primary,
            secondaryGraphLineColor = MaterialTheme.colorScheme.secondary,
            secondaryGraphLineAlpha = 0.5f,
            graphGradientColor = MaterialTheme.colorScheme.inversePrimary,
            graphGradientAlpha = 0.5f,
            valueTextStyle = ReplacementTheme.typography.small.copy(color = MaterialTheme.colorScheme.onSurface),
            timeTextStyle = ReplacementTheme.typography.extraSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        DailyChart(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh
                )
                .horizontalScroll(rememberScrollState())
                .height(240.dp)
                .width(1000.dp),
            data = dailySample.data,
            mainGraphLineColor = MaterialTheme.colorScheme.primary,
            mainGraphLineWidth = 16.dp,
            valueTextStyle = ReplacementTheme.typography.small.copy(color = MaterialTheme.colorScheme.onSurface),
            dateTextStyle = ReplacementTheme.typography.extraSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
    }
}
