package com.bunbeauty.smartweather.data

data class WeatherInfo(
    var coord: Coord = Coord(),
    var weather: ArrayList<Weather> = arrayListOf(),
    var base: String = "",
    var main: Main = Main(),
    var visibility: Long = 0L,
    var wind: Wind = Wind(),
    var clouds: Clouds = Clouds(),
    var dt: Long = 0L,
    var sys: Sys = Sys(),
    var timezone: Long = 0L,
    var id: Long = 0L,
    var name: String = "",
    var cod: Long = 0L
)