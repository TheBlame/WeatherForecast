package dev.maxim_v.weather_app.presentation.weather

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.maxim_v.weather_app.R
import dev.maxim_v.weather_app.domain.entity.SearchedLocation
import dev.maxim_v.weather_app.presentation.ui.theme.ReplacementTheme
import dev.maxim_v.weather_app.presentation.ui.theme.WeatherForecastTheme
import dev.maxim_v.weather_app.presentation.viewmodels.LocationSearchScreenEvent
import dev.maxim_v.weather_app.presentation.viewmodels.LocationSearchScreenState
import dev.maxim_v.weather_app.presentation.viewmodels.LocationSearchScreenVM
import kotlinx.coroutines.launch

@Composable
fun LocationSearchScreenRoot(
    onBackIconClick: (Unit) -> Unit,
    onBackPress: (Unit) -> Unit,
    onLocationSelected: (Boolean) -> Unit
) {
    val locationSearchVM: LocationSearchScreenVM = hiltViewModel()

    LocationSearchScreen(
        screenState = locationSearchVM.locationSearchScreenState,
        onEvent = locationSearchVM::onEvent,
        onBackIconClick = onBackIconClick,
        onBackPress= onBackPress,
        onLocationSelected = onLocationSelected
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationSearchScreen(
    screenState: LocationSearchScreenState,
    onEvent: (LocationSearchScreenEvent) -> Unit,
    onBackIconClick: (Unit) -> Unit,
    onBackPress: (Unit) -> Unit,
    onLocationSelected: (Boolean) -> Unit
) {
    var searchedText by remember {
        mutableStateOf("")
    }
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    BackHandler {
        onBackPress(Unit)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) },
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    TextField(
                        value = searchedText,
                        onValueChange = {
                            searchedText = it.trimStart()
                            onEvent(LocationSearchScreenEvent.Search(searchedText))
                        },
                        textStyle = ReplacementTheme.typography.medium,
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.location_search_placeholder),
                                style = ReplacementTheme.typography.small,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            autoCorrect = false,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
                        )
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
                        onBackIconClick(Unit)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                })
        }
    ) { innerPadding ->
        when (screenState) {
            is LocationSearchScreenState.Result -> {
                Text(modifier = Modifier.padding(innerPadding), text = "Result")
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = screenState.data
                    ) {
                        SearchResultItem(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    focusManager.clearFocus()
                                    scope.launch {
                                        onEvent(LocationSearchScreenEvent.SaveLocation(it))
                                        onLocationSelected(true)
                                    }
                                }
                                .background(color = MaterialTheme.colorScheme.surfaceContainer)
                                .padding(16.dp),
                            searchedLocation = it
                        )
                    }
                }
            }

            LocationSearchScreenState.Loading -> {
                ProgressIndicator(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(innerPadding)
                        .fillMaxSize()
                )
            }

            LocationSearchScreenState.Error -> {
                ErrorMessage(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(innerPadding)
                        .fillMaxSize()
                )
            }

            LocationSearchScreenState.Initial -> {}
            LocationSearchScreenState.NoResult -> {}

        }
    }
}

@Composable
private fun SearchResultItem(modifier: Modifier = Modifier, searchedLocation: SearchedLocation) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = searchedLocation.city,
            style = ReplacementTheme.typography.medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "${searchedLocation.country}, ${searchedLocation.area}",
            style = ReplacementTheme.typography.small,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
@Preview
fun PreviewLocationSearchScreen() {
    WeatherForecastTheme {
        LocationSearchScreen(
            LocationSearchScreenState.Result(
                data = listOf(
                    SearchedLocation(
                        "Russia",
                        "Moscow",
                        "Moscow obl",
                        0.0,
                        0.0
                    ),
                    SearchedLocation(
                        "Russia",
                        "Moscow",
                        "Moscow obl",
                        0.0,
                        0.0
                    ),
                    SearchedLocation(
                        "Russia",
                        "Moscow",
                        "Moscow obl",
                        0.0,
                        0.0
                    )
                )
            ),
            onEvent = {},
            onBackIconClick = {},
            onBackPress = {},
            onLocationSelected = {}
        )
    }
}