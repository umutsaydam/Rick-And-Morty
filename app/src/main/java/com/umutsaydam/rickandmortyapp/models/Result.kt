package com.umutsaydam.rickandmortyapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "characters")
data class Result(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val name: String = "",
    val status: String = "",
    val species: String = "",
    val type: String = "",
    val gender: String = "",
    val origin: Origin,
    val location: Location,
    val image: String = "",
    val episode: List<String> = emptyList(),
    val url: String = "",
    val created: String = "",
) : Serializable
