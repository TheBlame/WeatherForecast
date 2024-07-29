package dev.maxim_v.weather_app.presentation.ui.screens.forecastScreen

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import dev.maxim_v.weather_app.R
import dev.maxim_v.weather_app.domain.entity.CurrentForecast
import dev.maxim_v.weather_app.domain.entity.DailyForecast
import dev.maxim_v.weather_app.domain.entity.HourlyForecast
import dev.maxim_v.weather_app.presentation.ui.screens.misc.ErrorMessage
import dev.maxim_v.weather_app.presentation.ui.screens.misc.ProgressIndicator
import dev.maxim_v.weather_app.presentation.ui.theme.ReplacementTheme
import dev.maxim_v.weather_app.presentation.viewmodels.ForecastScreenError
import dev.maxim_v.weather_app.presentation.viewmodels.ForecastScreenEvent
import dev.maxim_v.weather_app.presentation.viewmodels.ForecastScreenState
import dev.maxim_v.weather_app.presentation.viewmodels.ForecastScreenVM
import dev.maxim_v.weather_app.util.checkLocationPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ForecastScreenRoot(
    onSettingIconClick: (Unit) -> Unit,
    onSearchIconClick: (Unit) -> Unit,
    needRefresh: Boolean?
) {
    val forecastScreenVM: ForecastScreenVM = hiltViewModel()

    ForecastScreen(
        screenState = forecastScreenVM.forecastScreenState,
        onEvent = forecastScreenVM::onEvent,
        onSettingIconClick = onSettingIconClick,
        onSearchIconClick = onSearchIconClick,
        errorFlow = forecastScreenVM.errorFlow,
        needRefresh = needRefresh
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ForecastScreen(
    screenState: ForecastScreenState,
    onEvent: (ForecastScreenEvent) -> Unit,
    onSettingIconClick: (Unit) -> Unit,
    onSearchIconClick: (Unit) -> Unit,
    needRefresh: Boolean?,
    errorFlow: SharedFlow<ForecastScreenError>
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var lastContent: ForecastScreenState by remember {
        mutableStateOf(ForecastScreenState.Initial)
    }

    LaunchedEffect(key1 = needRefresh) {
        if (needRefresh == true) onEvent(ForecastScreenEvent.Refresh)
    }

    SideEffect {
        if (screenState is ForecastScreenState.Content) lastContent = screenState
    }

    LaunchedEffect(key1 = Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                errorFlow.collectLatest {
                    val snackBarMessage = when (it) {
                        ForecastScreenError.NetworkError -> context.getString(R.string.network_error_main_screen)
                        ForecastScreenError.GpsAndNetworkError -> context.getString(R.string.gps_and_network_error)
                        ForecastScreenError.GpsError -> context.getString(R.string.gps_error)
                    }
                    snackbarHostState.showSnackbar(snackBarMessage)
                }
            }
        }
    }

    val locationPermissionRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                scope.launch {
                    onEvent(ForecastScreenEvent.GetLocationWithGps)
                }
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                scope.launch {
                    onEvent(ForecastScreenEvent.GetLocationWithGps)
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = (lastContent as? ForecastScreenState.Content)?.data?.location ?: "",
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
                        onSettingIconClick(Unit)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (checkLocationPermissions(context)
                        ) {
                            scope.launch {
                                onEvent(ForecastScreenEvent.GetLocationWithGps)
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
                    IconButton(onClick = { onSearchIconClick(Unit) }) {
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
            is ForecastScreenState.Content, ForecastScreenState.Loading -> {
                if (lastContent is ForecastScreenState.Content) {
                    ForecastScreenContent(
                        modifier = Modifier
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 32.dp, horizontal = 16.dp),
                        currentForecast = (lastContent as ForecastScreenState.Content).data.currentForecast,
                        hourlyForecast = (lastContent as ForecastScreenState.Content).data.hourlyForecast,
                        dailyForecast = (lastContent as ForecastScreenState.Content).data.dailyForecast
                    )
                }
                if (screenState is ForecastScreenState.Loading) {
                    ProgressIndicator(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxSize()
                    )
                }
            }

            ForecastScreenState.NoContent -> {
                ErrorMessage(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(innerPadding)
                        .fillMaxSize()
                )
            }

            ForecastScreenState.Initial -> {}
        }
    }
}

@Composable
private fun ForecastScreenContent(
    modifier: Modifier = Modifier,
    currentForecast: CurrentForecast,
    hourlyForecast: List<HourlyForecast>,
    dailyForecast: List<DailyForecast>
) {
    Column(
        modifier = modifier
    ) {
        CurrentWeatherCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            currentForecast = currentForecast
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = R.string.daily_graph_label),
            style = ReplacementTheme.typography.small,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        HourlyChart(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer
                )
                .horizontalScroll(rememberScrollState())
                .height(240.dp)
                .width(1800.dp),
            data = hourlyForecast,
            mainGraphLineColor = MaterialTheme.colorScheme.primary,
            secondaryGraphLineColor = MaterialTheme.colorScheme.secondary,
            secondaryGraphLineAlpha = 0.5f,
            graphGradientColor = MaterialTheme.colorScheme.inversePrimary,
            graphGradientAlpha = 0.5f,
            valueTextStyle = ReplacementTheme.typography.small.copy(color = MaterialTheme.colorScheme.onSurface),
            timeTextStyle = ReplacementTheme.typography.extraSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = R.string.weekly_graph_label),
            style = ReplacementTheme.typography.small,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        DailyChart(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer
                )
                .horizontalScroll(rememberScrollState())
                .height(240.dp)
                .width(1000.dp),
            data = dailyForecast,
            mainGraphLineColor = MaterialTheme.colorScheme.primary,
            mainGraphLineWidth = 16.dp,
            valueTextStyle = ReplacementTheme.typography.small.copy(color = MaterialTheme.colorScheme.onSurface),
            dateTextStyle = ReplacementTheme.typography.extraSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
    }
}
