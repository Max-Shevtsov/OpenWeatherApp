fun kelvinToCelsiusConverter(kelvinTemp: Double): String {
    val KELVIN_TO_CELSIUS = 273.15
    return "${(kelvinTemp - KELVIN_TO_CELSIUS).toUInt()} C"
}
