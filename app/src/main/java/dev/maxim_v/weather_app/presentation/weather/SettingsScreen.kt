package dev.maxim_v.weather_app.presentation.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.maxim_v.weather_app.R
import dev.maxim_v.weather_app.domain.entity.enums.TemperatureUnit.CELSIUS
import dev.maxim_v.weather_app.domain.entity.enums.TemperatureUnit.FAHRENHEIT
import dev.maxim_v.weather_app.domain.entity.enums.ThemeType
import dev.maxim_v.weather_app.domain.entity.enums.WindSpeedUnit.KMH
import dev.maxim_v.weather_app.domain.entity.enums.WindSpeedUnit.MS
import dev.maxim_v.weather_app.presentation.ui.theme.ReplacementTheme
import dev.maxim_v.weather_app.presentation.ui.theme.WeatherForecastTheme
import dev.maxim_v.weather_app.util.getString
import dev.maxim_v.weather_app.util.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings),
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

                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }

            )
        },
    ) { innerPadding ->
        SettingsScreenContent(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val tempOptions = remember {
        listOf(CELSIUS, FAHRENHEIT)
    }

    val tempItems = remember {
        tempOptions.map { it.getString(context) }
    }

    var tempSelectedIndex by remember {
        mutableIntStateOf(0)
    }

    val speedOptions = remember {
        listOf(MS, KMH)
    }

    val speedItems = remember {
        speedOptions.map { it.getString(context) }
    }

    var speedSelectedIndex by remember {
        mutableIntStateOf(0)
    }

    val themeOptions = listOf(
        ThemeType.AUTO, ThemeType.LIGHT, ThemeType.DARK
    )

    val (selectedTheme, onThemeSelected) = remember {
        mutableStateOf(themeOptions[0])
    }

    var notificationWidget by remember {
        mutableStateOf(false)
    }

    Row(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp)
        ) {

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(id = R.string.units),
                style = ReplacementTheme.typography.small,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
            ) {
                TextWithTextSwitch(
                    text = stringResource(id = R.string.temperature),
                    items = tempItems,
                    selectedIndex = tempSelectedIndex,
                    onSelectionChange = {
                        tempSelectedIndex = it
                    })
                Spacer(modifier = Modifier.height(16.dp))
                TextWithTextSwitch(
                    text = stringResource(id = R.string.wind_speed),
                    items = speedItems,
                    selectedIndex = speedSelectedIndex,
                    onSelectionChange = {
                        speedSelectedIndex = it
                    })
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(id = R.string.theme),
                style = ReplacementTheme.typography.small,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
                    .fillMaxWidth()
                    .selectableGroup()
            ) {
                themeOptions.forEachIndexed { index, theme ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (theme == selectedTheme),
                                onClick = { onThemeSelected(theme) },
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                            RadioButton(
                                selected = (theme == selectedTheme),
                                onClick = { onThemeSelected(theme) })
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = theme.stringResource(),
                            style = ReplacementTheme.typography.small,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    if (index != themeOptions.size - 1) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(id = R.string.notification_panel),
                style = ReplacementTheme.typography.small,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.show_widget),
                    style = ReplacementTheme.typography.small,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Switch(
                    checked = notificationWidget,
                    onCheckedChange = { notificationWidget = it }
                )
            }
        }

    }
}

@Preview
@Composable
fun PreviewSettingsScreen() {
    WeatherForecastTheme {
        SettingsScreen()
    }
}