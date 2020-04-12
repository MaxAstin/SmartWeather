package com.bunbeauty.smartweather.di

import com.bunbeauty.smartweather.view.WeatherActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(weatherActivity: WeatherActivity)
}