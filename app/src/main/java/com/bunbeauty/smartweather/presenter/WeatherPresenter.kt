package com.bunbeauty.smartweather.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.bunbeauty.smartweather.business.WeatherInteractor
import com.bunbeauty.smartweather.data.WeatherInfo
import com.bunbeauty.smartweather.view.WeatherView

@InjectViewState
class WeatherPresenter(private val weatherInteractor: WeatherInteractor) :
    MvpPresenter<WeatherView>(), WeatherPresenterCallback {

    fun getCityNames(country: String) {
        weatherInteractor.getCityNames(country, this)
    }

    override fun setCityNames(cityNames: List<String>) {
        viewState.setCityNames(cityNames)
    }

    fun getWeather(city: String) {
        weatherInteractor.getWeather(city, this)
    }

    override fun showWeather(weatherInfo: WeatherInfo) {
        viewState.showWeather(weatherInfo)
        viewState.showAdvice(weatherInteractor.getAdvice(weatherInfo))
    }
}