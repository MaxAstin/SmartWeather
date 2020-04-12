package com.bunbeauty.smartweather.view

import com.arellomobile.mvp.MvpView
import com.bunbeauty.smartweather.data.WeatherInfo


interface WeatherView: MvpView {
    fun showWeather(weatherInfo: WeatherInfo)
    fun showAdvice(advice: String)
    fun setCityNames(cityNames: List<String>)
}