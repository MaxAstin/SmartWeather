package com.bunbeauty.smartweather.view

import android.os.Bundle
import android.view.View
import android.widget.*
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.bunbeauty.smartweather.R
import com.bunbeauty.smartweather.business.WeatherInteractor
import com.bunbeauty.smartweather.data.WeatherInfo
import com.bunbeauty.smartweather.di.AppModule
import com.bunbeauty.smartweather.di.DaggerAppComponent
import com.bunbeauty.smartweather.presenter.WeatherPresenter
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import javax.inject.Inject

class WeatherActivity : MvpAppCompatActivity(), WeatherView {

    private lateinit var citySpinner: Spinner
    private lateinit var temperatureText: TextView
    private lateinit var iconImageView: ImageView
    private lateinit var stateText: TextView
    private lateinit var feelsLikeText: TextView
    private lateinit var adviceText: TextView

    @InjectPresenter
    lateinit var weatherPresenter: WeatherPresenter

    @Inject
    lateinit var weatherInteractor: WeatherInteractor

    @ProvidePresenter
    internal fun provideWeatherPresenter(): WeatherPresenter {
        DaggerAppComponent
            .builder()
            .appModule(AppModule(application))
            .build().inject(this)

        return WeatherPresenter(weatherInteractor)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        init()

        weatherPresenter.getWeather("Moscow")
        setCitySpinner(arrayListOf("Moscow"))
        setCitySpinnerListener()

        weatherPresenter.getCityNames("RU")
    }

    private fun init() {
        citySpinner = findViewById(R.id.cityWeatherSpinner)
        temperatureText = findViewById(R.id.temperatureWeatherText)
        iconImageView = findViewById(R.id.iconWeatherImageView)
        stateText = findViewById(R.id.stateWeatherText)
        feelsLikeText = findViewById(R.id.feelsLikeWeatherText)
        adviceText = findViewById(R.id.adviceWeatherText)
    }

    override fun setCityNames(cityNames: List<String>) {
        setCitySpinner(cityNames)
    }

    private fun setCitySpinner(cityNames: List<String>) = runOnUiThread {
        citySpinner.adapter =
            ArrayAdapter(this, R.layout.custom_spinner_dropdown_item, cityNames)
    }

    private fun setCitySpinnerListener() {
        citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                itemSelected: View?,
                selectedItemPosition: Int,
                selectedId: Long
            ) {
                val selectedCity = (itemSelected as TextView).text.toString()
                weatherPresenter.getWeather(selectedCity)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    override fun showWeather(weatherInfo: WeatherInfo) = runOnUiThread {
        val tempCalvinDegrees = weatherPresenter.convertToCalvin(weatherInfo.main.temp)
        temperatureText.text = formatDegrees(tempCalvinDegrees)

        if (weatherInfo.weather.size > 0) {
            val iconName = weatherInfo.weather[0].icon
            Picasso.get()
                .load("http://openweathermap.org/img/wn/$iconName@2x.png")
                .fit()
                .centerCrop()
                .into(iconImageView)


            stateText.text = weatherInfo.weather[0].description
        }

        val feelsLikeCalvinDegrees = weatherPresenter.convertToCalvin(weatherInfo.main.feels_like)
        feelsLikeText.text = FEELS_LIKE + formatDegrees(feelsLikeCalvinDegrees)
    }

    private fun formatDegrees(degrees: Double): String {
        val decimalFormat = DecimalFormat("#")

        return "${decimalFormat.format(degrees)}°C"
    }

    override fun showAdvice(advice: String) = runOnUiThread {
        adviceText.text = advice
    }

    companion object {
        const val FEELS_LIKE = "Feels like "
    }
}