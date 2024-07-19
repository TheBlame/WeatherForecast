package dev.maxim_v.weather_app.presentation.weather

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
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.maxim_v.weather_app.R
import dev.maxim_v.weather_app.domain.entity.TemperatureUnit.CELSIUS
import dev.maxim_v.weather_app.domain.entity.TemperatureUnit.FAHRENHEIT
import dev.maxim_v.weather_app.domain.entity.ThemeType
import dev.maxim_v.weather_app.domain.entity.WindSpeedUnit.KMH
import dev.maxim_v.weather_app.domain.entity.WindSpeedUnit.MS
import dev.maxim_v.weather_app.presentation.ui.theme.ReplacementTheme
import dev.maxim_v.weather_app.presentation.ui.theme.WeatherForecastTheme
import dev.maxim_v.weather_app.util.getString
import dev.maxim_v.weather_app.util.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSheet(modifier: Modifier = Modifier) {

    ModalDrawerSheet(
        modifier = modifier,
        drawerContainerColor = MaterialTheme.colorScheme.background,
        drawerShape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 12.dp,
            bottomStart = 0.dp,
            bottomEnd = 12.dp
        )
    ) {
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.options),
                style = ReplacementTheme.typography.medium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
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

@Composable
fun TextWithTextSwitch(
    modifier: Modifier = Modifier,
    text: String,
    items: List<String>,
    selectedIndex: Int,
    onSelectionChange: (Int) -> Unit
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
                    .height(24.dp)
                    .width(72.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(2.dp),
                selectedIndex = selectedIndex,
                items = items,
                onSelectionChange = onSelectionChange,
                textStyle = ReplacementTheme.typography.extraSmall,
                activeSwitchCornerRadius = 12.dp,
                activeTextColor = MaterialTheme.colorScheme.inverseSurface,
                activeSwitchColor = MaterialTheme.colorScheme.onPrimary,
                inactiveTextColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}


@Preview
@Composable
fun PreviewSettingsSheet() {
    WeatherForecastTheme(false) {
        SettingsSheet(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxHeight()
                .width(IntrinsicSize.Max)

        )
    }
}

@Composable
private fun TextSwitch(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    items: List<String>,
    onSelectionChange: (Int) -> Unit,
    textStyle: TextStyle = LocalTextStyle.current,
    activeTextColor: Color = Color.Black,
    inactiveTextColor: Color = Color.Unspecified,
    activeSwitchColor: Color = Color.White,
    activeSwitchCornerRadius: Dp = 0.dp
) {

    BoxWithConstraints(
        modifier = modifier
    ) {
        if (items.isNotEmpty()) {

            val maxWidth = this.maxWidth
            val tabWidth = maxWidth / items.size

            val animationOffset by animateDpAsState(
                targetValue = tabWidth * selectedIndex,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
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
                items.forEachIndexed { index, text ->

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
                                    if (items.size == 2) {
                                        val result =
                                            if (index == selectedIndex && selectedIndex == 0) {
                                                1
                                            } else if (index == selectedIndex && selectedIndex == 1) {
                                                0
                                            } else {
                                                index
                                            }
                                        onSelectionChange(result)
                                    } else {
                                        onSelectionChange(index)
                                    }

                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = text,
                            color = inactiveTextColor,
                            style = textStyle
                        )
                    }
                }
            }
        }
    }
}