package com.example.model

enum class GameType(val displayName: String, val subtitle: String, val urlPattern: String) {
    MINES(
        displayName = "1Win Mines",
        subtitle = "5x5 Diamond Grid Combinatorics",
        urlPattern = "mines"
    ),
    DOUBLE_DRUM(
        displayName = "1Win Double",
        subtitle = "Color Drum & Multiplier Wheel (x2 / x14)",
        urlPattern = "double"
    );

    companion object {
        fun detectFromUrl(url: String): GameType {
            val lower = url.lowercase()
            return when {
                lower.contains("double") -> DOUBLE_DRUM
                lower.contains("mines") -> MINES
                else -> MINES
            }
        }
    }
}
