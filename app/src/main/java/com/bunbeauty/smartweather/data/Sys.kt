package com.bunbeauty.smartweather.data

data class Sys(
    var type: Int = 0,
    var id: Int = 0,
    var country: String = "",
    var sunrise: Int = 0,
    var sunset: Int = 0
)