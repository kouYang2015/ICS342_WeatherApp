package ics340kyang.weatherapp

data class CurrentConditions(
    val weather: List<WeatherCondition>,
    val main: Currents,
    val name: String
)
