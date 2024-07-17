package dev.maxim_v.weather_app.presentation.weather

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.maxim_v.weather_app.domain.entity.WeatherModel
import dev.maxim_v.weather_app.domain.entity.WeatherSample.CURRENT
import dev.maxim_v.weather_app.domain.entity.WeatherSample.DAILY
import dev.maxim_v.weather_app.domain.entity.WeatherSample.HOURLY
import dev.maxim_v.weather_app.presentation.ui.theme.ReplacementTheme
import dev.maxim_v.weather_app.presentation.viewmodels.MainScreenState
import dev.maxim_v.weather_app.presentation.viewmodels.MainScreenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(modifier: Modifier = Modifier, viewModel: MainScreenViewModel = viewModel()) {
    val screenState = viewModel.mainScreenState.collectAsState(MainScreenState.Initial)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet { /* Drawer content */ }
    }) {
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
                        IconButton(onClick = { /*TODO*/ }) {
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
            }
        ) { innerPadding ->
            when (val currentState = screenState.value) {
                is MainScreenState.Success -> {
                    Forecast(
                        modifier = modifier
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState()),
                        currentSample = currentState.data[CURRENT] as WeatherModel.CurrentSample,
                        hourlySample = currentState.data[HOURLY] as WeatherModel.HourlySample,
                        dailySample = currentState.data[DAILY] as WeatherModel.DailySample
                    )
                }

                MainScreenState.Error -> {}
                MainScreenState.Initial -> {}
                MainScreenState.Loading -> {}
            }
        }
    }


}