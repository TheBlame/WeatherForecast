package dev.maxim_v.weather_app.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxim_v.weather_app.domain.entity.WeatherSample.CURRENT
import dev.maxim_v.weather_app.domain.entity.WeatherSample.DAILY
import dev.maxim_v.weather_app.domain.entity.WeatherSample.HOURLY
import dev.maxim_v.weather_app.domain.usecases.GetLocationWithGpsUseCase
import dev.maxim_v.weather_app.domain.usecases.GetWeatherUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getLocationWithGpsUseCase: GetLocationWithGpsUseCase
) : ViewModel() {

    private val _mainScreenState: MutableStateFlow<MainScreenState> = MutableStateFlow(MainScreenState.Initial)
    val mainScreenState = _mainScreenState.asStateFlow()

    init {
        viewModelScope.launch {
            getWeatherUseCase(CURRENT, HOURLY, DAILY)
                .onStart { _mainScreenState.value = MainScreenState.Loading }
                .collectLatest {
                _mainScreenState.value = MainScreenState.Success(it)
            }
        }
    }

    suspend fun getLocationWithGps() {
        getLocationWithGpsUseCase.invoke()
        getWeatherUseCase(CURRENT, HOURLY, DAILY)
            .onStart { _mainScreenState.value = MainScreenState.Loading }
            .collectLatest {
                _mainScreenState.value = MainScreenState.Success(it)
            }
    }
}