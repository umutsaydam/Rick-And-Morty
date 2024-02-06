package com.umutsaydam.rickandmortyapp.db

import androidx.room.TypeConverter
import com.umutsaydam.rickandmortyapp.models.Location
import com.umutsaydam.rickandmortyapp.models.Origin

class Converters {

    @TypeConverter
    fun fromOrigin(origin: Origin): String {
        return origin.name
    }

    @TypeConverter
    fun toOrigin(name: String): Origin {
        return Origin(name, name)
    }

    @TypeConverter
    fun fromLocation(location: Location): String {
        return location.name
    }

    @TypeConverter
    fun toLocation(name: String): Location {
        return Location(name, name)
    }

    @TypeConverter
    fun fromEpisode(episode: List<String>): String {
        return ""
    }

    @TypeConverter
    fun toEpisode(episode: String): List<String> {
        return emptyList()
    }
}