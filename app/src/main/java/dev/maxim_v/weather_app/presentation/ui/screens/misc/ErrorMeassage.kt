package dev.maxim_v.weather_app.presentation.ui.screens.misc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.maxim_v.weather_app.R
import dev.maxim_v.weather_app.presentation.ui.theme.ReplacementTheme

@Composable
fun ErrorMessage(modifier: Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(128.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            painter = painterResource(id = R.drawable.baseline_wifi_off_24),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.wrapContentSize(),
            text = stringResource(id = R.string.network_error_search_screen),
            style = ReplacementTheme.typography.medium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}