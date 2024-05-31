interface LoadIcon {
    fun load(icon:String?): String {
        Log.e("!!!", "weatherType:$icon")
        return "https://openweathermap.org/img/wn/$icon@2x.png"
    }
}