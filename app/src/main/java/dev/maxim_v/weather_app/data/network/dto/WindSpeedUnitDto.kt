package dev.maxim_v.weather_app.data.network.dto

import com.google.gson.annotations.SerializedName
import dev.maxim_v.weather_app.domain.entity.enums.WindSpeedUnit

enum class WindSpeedUnitDto(val unit: WindSpeedUnit) {
    @SerializedName("m/s")
    MS_DTO(WindSpeedUnit.MS),
    @SerializedName("km/h")
    KMH_DTO(WindSpeedUnit.KMH)
}