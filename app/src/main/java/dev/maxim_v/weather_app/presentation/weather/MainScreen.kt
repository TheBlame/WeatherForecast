package dev.maxim_v.weather_app.presentation.weather

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import dev.maxim_v.weather_app.R
import dev.maxim_v.weather_app.domain.entity.CurrentForecast
import dev.maxim_v.weather_app.domain.entity.DailyForecast
import dev.maxim_v.weather_app.domain.entity.HourlyForecast
import dev.maxim_v.weather_app.presentation.ui.theme.ReplacementTheme
import dev.maxim_v.weather_app.presentation.viewmodels.MainScreenError
import dev.maxim_v.weather_app.presentation.viewmodels.MainScreenEvent
import dev.maxim_v.weather_app.presentation.viewmodels.MainScreenState
import dev.maxim_v.weather_app.presentation.viewmodels.MainScreenViewModel
import dev.maxim_v.weather_app.util.checkLocationPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MainScreenRoot(onSettingIconClick: (Unit) -> Unit, needRefresh: Boolean?) {
    val mainScreenViewModel: MainScreenViewModel = hiltViewModel()

    MainScreen(
        screenState = mainScreenViewModel.mainScreenState,
        onEvent = mainScreenViewModel::onEvent,
        onSettingIconClick = onSettingIconClick,
        errorFlow = mainScreenViewModel.errorFlow,
        needRefresh = needRefresh
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
    screenState: MainScreenState,
    onEvent: (MainScreenEvent) -> Unit,
    onSettingIconClick: (Unit) -> Unit,
    needRefresh: Boolean?,
    errorFlow: SharedFlow<MainScreenError>
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var lastContent: MainScreenState by remember {
        mutableStateOf(MainScreenState.Initial)
    }

    LaunchedEffect(key1 = needRefresh) {
        if (needRefresh == true) onEvent(MainScreenEvent.Refresh)
    }

    SideEffect {
        if (screenState is MainScreenState.Content) lastContent = screenState
    }

    LaunchedEffect(key1 = Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                errorFlow.collectLatest {
                    val snackBarMessage = when (it) {
                        MainScreenError.NetworkError -> context.getString(R.string.network_error)
                        MainScreenError.GpsAndNetworkError -> context.getString(R.string.gps_and_network_error)
                        MainScreenError.GpsError -> context.getString(R.string.gps_error)
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = (lastContent as? MainScreenState.Content)?.data?.location ?: "",
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
            is MainScreenState.Content, MainScreenState.Loading -> {
                if (lastContent is MainScreenState.Content) {
                    MainScreenContent(
                        modifier = Modifier
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 32.dp, horizontal = 16.dp),
                        currentForecast = (lastContent as MainScreenState.Content).data.currentForecast,
                        hourlyForecast = (lastContent as MainScreenState.Content).data.hourlyForecast,
                        dailyForecast = (lastContent as MainScreenState.Content).data.dailyForecast
                    )
                }
                if (screenState is MainScreenState.Loading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            MainScreenState.NoContent -> {}
            MainScreenState.Initial -> {}
        }
    }
}

@Composable
private fun MainScreenContent(
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
        Spacer(modifier = Modifier.height(16.dp))
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
        Spacer(modifier = Modifier.height(16.dp))
        DailyChart(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh
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
