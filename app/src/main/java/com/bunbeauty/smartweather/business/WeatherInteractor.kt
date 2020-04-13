package com.bunbeauty.smartweather.business

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import com.bunbeauty.smartweather.R
import com.bunbeauty.smartweather.data.City
import com.bunbeauty.smartweather.data.WeatherInfo
import com.bunbeauty.smartweather.presenter.WeatherPresenterCallback
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.lang.StringBuilder
import java.util.*

class WeatherInteractor(private val context: Context) {

    private val client = OkHttpClient()

    fun getCityNames(country: String, weatherPresenterCallback: WeatherPresenterCallback) {
        CityNamesLoader(country, weatherPresenterCallback).execute()
    }

    fun getWeather(city: String, weatherPresenterCallback: WeatherPresenterCallback) {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$API_KEY"

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val result = response.body()!!.string()
                val weatherInfo = Gson().fromJson(result, WeatherInfo::class.java)
                weatherPresenterCallback.showWeather(weatherInfo)
            }
        })
    }

    fun getAdvice(weatherInfo: WeatherInfo): String {
        val adviceStringBuilder = StringBuilder("Советуем надеть ")

        adviceStringBuilder.append(getTemperatureAnalysis(weatherInfo.main.temp))
        if (weatherInfo.weather.size > 0) {
            adviceStringBuilder.append(getPrecipitationAnalysis(weatherInfo.weather[0].description))
        }

        return adviceStringBuilder.toString()
    }

    private fun getTemperatureAnalysis(temp: Double): String {
        when (convertToCalvin(temp).toInt()) {
            in -50..0 -> {
                return "тёплую шапку с перчатками.\n"
            }
            in 1..10 -> {
                return "свитер или кофту.\n"
            }
            in 11..20 -> {
                return "лёгкую кофту или рубашку.\n"
            }
            in 21..50 -> {
                return "футболку или поло.\n"
            }
            else -> {
                return "скафандр.\n"
            }
        }
    }

    private fun getPrecipitationAnalysis(weatherDescription: String): String {
        if (weatherDescription.contains("rain")) {
            return "И не забудьте зонт"
        } else if (weatherDescription == "clear sky") {
            return "И не забудьте солнечные очки"
        }

        return ""
    }

    fun convertToCalvin(fahrenheitDegrees: Double): Double {
        return fahrenheitDegrees - 273.15
    }

    @SuppressLint("StaticFieldLeak")
    inner class CityNamesLoader(
        private val country: String,
        private val weatherPresenterCallback: WeatherPresenterCallback
    ) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            val jsonString =
                context.resources.openRawResource(R.raw.city_list).bufferedReader()
                    .use { it.readText() }

            val cityNames =
                LinkedList(
                    Gson().fromJson(
                        jsonString,
                        Array<City>::class.java
                    ).asList().mapNotNull {
                        if (it.country == country) {
                            it.name
                        } else {
                            null
                        }
                    })
            cityNames.add(0, "Moscow")
            weatherPresenterCallback.setCityNames(cityNames)

            return null
        }
    }

    companion object {
        const val API_KEY = "c191e0937c29a7560794e97bd1a969f3"
    }
}