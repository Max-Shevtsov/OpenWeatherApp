import android.util.Log
import android.widget.ImageView
import coil.load



fun loadWeatherTypePicture(icon: String?, uiItem: ImageView) {
    var url = "https://openweathermap.org/img/wn/$icon@2x.png"
    uiItem.load(url)
}

fun kelvinToCelsiusConverter(kelvinTemp: Double): String {
    val KELVIN_TO_CELSIUS = 273.15
    return "${(kelvinTemp - KELVIN_TO_CELSIUS).toUInt()} C"
}
