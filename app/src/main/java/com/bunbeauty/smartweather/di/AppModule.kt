package com.bunbeauty.smartweather.di

import android.app.Application
import com.bunbeauty.smartweather.business.WeatherInteractor
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: Application) {
    @Provides
    fun provideWeatherInteractor() = WeatherInteractor(application)
}