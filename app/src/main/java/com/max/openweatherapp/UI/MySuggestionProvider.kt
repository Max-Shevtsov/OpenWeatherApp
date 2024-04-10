package com.max.openweatherapp.UI
import android.content.SerachRecentSuggestionsProvider

class MySuggestionProvider : SerachRecentSuggestionsProvider {
    val AUTHORITY = "com.max.openweatherapp.MySuggestionProvider"
    val MODE = DATABASE_MODE_QUERIES

    MySuggestionProvider() {
        setupSuggestion(AUTHORITY, MODE)
    }
}