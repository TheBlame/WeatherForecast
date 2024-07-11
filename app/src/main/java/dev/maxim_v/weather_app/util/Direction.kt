package dev.maxim_v.weather_app.util

enum class Direction {
    NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST;

    companion object {

        operator fun get(degree: Int): Direction {
            return when (degree) {
                in 22..<67 -> NORTH_EAST
                in 67..<112 -> EAST
                in 112..<157 -> SOUTH_EAST
                in 157..<202 -> SOUTH
                in 202..<247 -> SOUTH_WEST
                in 247..<292 -> WEST
                in 292..<337 -> NORTH_WEST
                else -> NORTH
            }
        }
    }
}