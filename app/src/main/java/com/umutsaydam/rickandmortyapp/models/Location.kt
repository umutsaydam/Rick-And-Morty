package com.umutsaydam.rickandmortyapp.models

import java.io.Serializable

data class Location(
    val name: String = "",
    val url: String = "",
): Serializable
