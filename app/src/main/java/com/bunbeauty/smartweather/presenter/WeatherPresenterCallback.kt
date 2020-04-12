package com.bunbeauty.smartweather.presenter

import com.bunbeauty.smartweather.data.WeatherInfo

interface WeatherPresenterCallback {
    fun showWeather(weatherInfo: WeatherInfo)
    fun setCityNames(cityNames: List<String>)
}