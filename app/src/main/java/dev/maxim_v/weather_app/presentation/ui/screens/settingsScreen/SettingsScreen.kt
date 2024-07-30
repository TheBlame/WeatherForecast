package dev.maxim_v.weather_app.presentation.ui.screens.settingsScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.maxim_v.weather_app.R
import dev.maxim_v.weather_app.domain.entity.UserSettings
import dev.maxim_v.weather_app.domain.entity.enums.TemperatureUnit.CELSIUS
import dev.maxim_v.weather_app.domain.entity.enums.ThemeType
import dev.maxim_v.weather_app.domain.entity.enums.WindSpeedUnit.MS
import dev.maxim_v.weather_app.presentation.ui.theme.ReplacementTheme
import dev.maxim_v.weather_app.presentation.ui.theme.WeatherForecastTheme
import dev.maxim_v.weather_app.presentation.viewmodels.SettingsScreenEvent
import dev.maxim_v.weather_app.presentation.viewmodels.SettingsScreenState
import dev.maxim_v.weather_app.presentation.viewmodels.SettingsScreenVM
import dev.maxim_v.weather_app.util.getString
import dev.maxim_v.weather_app.util.stringResource

@Composable
fun SettingsScreenRoot(
    onBackIconClick: (Boolean) -> Unit,
    onBackPress: (Boolean) -> Unit
) {
    val settingsScreenVM: SettingsScreenVM = hiltViewModel()
    val settingsScreenState by settingsScreenVM.settingsScreenState.collectAsStateWithLifecycle()

    SettingsScreen(
        settingsScreenState = settingsScreenState,
        onBackIconClick = onBackIconClick,
        onBackPress = onBackPress,
        onEvent = settingsScreenVM::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    settingsScreenState: SettingsScreenState,
    onEvent: (SettingsScreenEvent) -> Unit,
    onBackIconClick: (Boolean) -> Unit,
    onBackPress: (Boolean) -> Unit
) {
    var needMainScreenRefresh by remember {
        mutableStateOf(false)
    }

    BackHandler {
        onBackPress(needMainScreenRefresh)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
                        onBackIconClick(needMainScreenRefresh)
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
        when (settingsScreenState) {
            is SettingsScreenState.Ready -> {
                SettingsScreenContent(
                    modifier = Modifier
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 32.dp, horizontal = 16.dp),
                    settings = settingsScreenState.setting,
                    onEvent = onEvent,
                    needMainScreenRefresh = { needMainScreenRefresh = it }
                )
            }

            SettingsScreenState.Initial -> {}
        }
    }
}

@Composable
fun SettingsScreenContent(
    modifier: Modifier = Modifier,
    settings: UserSettings,
    onEvent: (SettingsScreenEvent) -> Unit,
    needMainScreenRefresh: (Boolean) -> Unit
) {
    val context = LocalContext.current

    val tempOptions = remember {
        settings.tempUnit::class.java.enumConstants.toList()
    }

    val tempItems = remember {
        tempOptions.map { it.getString(context) }
    }

    val speedOptions = remember {
        settings.windSpeedUnit::class.java.enumConstants.toList()
    }

    val speedItems = remember {
        speedOptions.map { it.getString(context) }
    }

    val themeOptions = remember {
        settings.theme::class.java.enumConstants.toList()
    }

    Row(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
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
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(12.dp)
            ) {
                TextWithTextSwitch(
                    text = stringResource(id = R.string.temperature),
                    items = tempOptions,
                    itemNames = tempItems,
                    selectedItem = settings.tempUnit,
                    onSelectionChange = {
                        onEvent(SettingsScreenEvent.SettingUpdate(settings.copy(tempUnit = it)))
                        needMainScreenRefresh(true)
                    })
                Spacer(modifier = Modifier.height(16.dp))
                TextWithTextSwitch(
                    text = stringResource(id = R.string.wind_speed),
                    items = speedOptions,
                    itemNames = speedItems,
                    selectedItem = settings.windSpeedUnit,
                    onSelectionChange = {
                        onEvent(SettingsScreenEvent.SettingUpdate(settings.copy(windSpeedUnit = it)))
                        needMainScreenRefresh(true)
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
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(12.dp)
                    .fillMaxWidth()
                    .selectableGroup()
            ) {
                themeOptions.forEachIndexed { index, theme ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .selectable(
                                selected = (theme == settings.theme),
                                onClick = {
                                    onEvent(
                                        SettingsScreenEvent
                                            .SettingUpdate(settings.copy(theme = theme))
                                    )
                                }),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CompositionLocalProvider(
                            LocalMinimumInteractiveComponentSize provides Dp.Unspecified
                        ) {
                            RadioButton(
                                selected = (theme == settings.theme),
                                onClick = {
                                    onEvent(
                                        SettingsScreenEvent
                                            .SettingUpdate(settings.copy(theme = theme))
                                    )
                                })
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
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = R.string.show_widget),
                        style = ReplacementTheme.typography.small,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(id = R.string.notification_hint),
                        style = ReplacementTheme.typography.extraSmall.copy(lineBreak = LineBreak.Heading),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Switch(
                    modifier = Modifier.wrapContentSize(),
                    checked = settings.notification,
                    onCheckedChange = {
                        onEvent(SettingsScreenEvent.SettingUpdate(settings.copy(notification = it)))
                    }
                )
            }
        }
    }
}

@Composable
fun <T> TextWithTextSwitch(
    modifier: Modifier = Modifier,
    text: String,
    items: List<T>,
    itemNames: List<String>,
    selectedItem: T,
    onSelectionChange: (T) -> Unit
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                style = ReplacementTheme.typography.small,
                color = MaterialTheme.colorScheme.onSurface
            )
            TextSwitch(
                modifier = Modifier
                    .height(32.dp)
                    .width(104.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(4.dp),
                selectedItem = selectedItem,
                items = items,
                itemNames = itemNames,
                onSelectionChange = onSelectionChange,
                textStyle = ReplacementTheme.typography.extraSmall,
                activeSwitchCornerRadius = 16.dp,
                activeTextColor = MaterialTheme.colorScheme.inverseSurface,
                activeSwitchColor = MaterialTheme.colorScheme.onPrimary,
                inactiveTextColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun <T> TextSwitch(
    modifier: Modifier = Modifier,
    selectedItem: T,
    items: List<T>,
    itemNames: List<String>,
    onSelectionChange: (T) -> Unit,
    textStyle: TextStyle = LocalTextStyle.current,
    activeTextColor: Color = Color.Black,
    inactiveTextColor: Color = Color.Unspecified,
    activeSwitchColor: Color = Color.White,
    activeSwitchCornerRadius: Dp = 0.dp
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        if (itemNames.isNotEmpty()) {

            val maxWidth = this.maxWidth
            val tabWidth = maxWidth / itemNames.size

            val animationOffset by animateDpAsState(
                targetValue = tabWidth * items.indexOf(selectedItem),
                animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing),
                label = "animationOffset"
            )

            Row(modifier = Modifier
                .fillMaxWidth()
                .drawWithContent {

                    drawRoundRect(
                        topLeft = Offset(x = animationOffset.toPx(), 0f),
                        size = Size(size.width / 2, size.height),
                        color = activeTextColor,
                        cornerRadius = CornerRadius(
                            x = activeSwitchCornerRadius.toPx(),
                            y = activeSwitchCornerRadius.toPx()
                        ),
                    )

                    with(drawContext.canvas.nativeCanvas) {
                        val checkPoint = saveLayer(null, null)
                        drawContent()
                        drawRoundRect(
                            topLeft = Offset(x = animationOffset.toPx(), 0f),
                            size = Size(size.width / 2, size.height),
                            color = activeSwitchColor,
                            cornerRadius = CornerRadius(
                                x = activeSwitchCornerRadius.toPx(),
                                y = activeSwitchCornerRadius.toPx()
                            ),
                            blendMode = BlendMode.SrcOut
                        )
                        restoreToCount(checkPoint)
                    }
                }
            ) {
                items.forEachIndexed { index, t ->

                    Box(
                        modifier = Modifier
                            .width(tabWidth)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                indication = null,
                                onClick = {
                                    if (t == selectedItem) {
                                        onSelectionChange(
                                            items.getOrNull(index + 1) ?: items[index - 1]
                                        )
                                    } else {
                                        onSelectionChange(t)
                                    }
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = itemNames[index],
                            color = inactiveTextColor,
                            style = textStyle
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewSettingsScreen() {
    WeatherForecastTheme {
        SettingsScreen(
            settingsScreenState = SettingsScreenState.Ready(
                UserSettings(
                    CELSIUS,
                    MS,
                    ThemeType.SYSTEM,
                    true
                )
            ),
            onEvent = {},
            onBackIconClick = {},
            onBackPress = {}
        )
    }
}