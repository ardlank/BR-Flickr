package com.example.br_flickr.util

import android.content.SearchRecentSuggestionsProvider

//Set up SearchRecentSuggestionsProvider for recent queries
class MySuggestionProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(
            AUTHORITY,
            MODE
        )
    }

    companion object {
        const val AUTHORITY = "com.example.MySuggestionProvider"
        const val MODE: Int = DATABASE_MODE_QUERIES
    }
}