package com.bunbeauty.smartweather.data

data class Main(
    var temp: Double = 0.0,
    var feels_like: Double = 0.0,
    var temp_min: Double = 0.0,
    var temp_max: Double = 0.0,
    var pressure: Int = 0,
    var humidity: Int = 0
)