fun kelvinToCelsiusConverter(kelvinTemp: Double?): String {
    val KELVIN_TO_CELSIUS = 273.15
    if (kelvinTemp != null) {
        return "${(kelvinTemp - KELVIN_TO_CELSIUS).toUInt()} C"
    } else return "X_X"

}
