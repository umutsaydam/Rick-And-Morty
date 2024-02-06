package com.umutsaydam.rickandmortyapp.api

import com.umutsaydam.rickandmortyapp.models.Root
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyAPI {
    @GET("character")
    suspend fun getCharacters(
        @Query("page")
        charactersPage: Int
    ): Response<Root>

    @GET("character/")
    suspend fun getFilteredCharacters(
        @Query("name")
        characterName: String,
        @Query("status")
        characterStatus: String,
        @Query("species")
        characterSpecies: String,
        @Query("type")
        characterType: String,
        @Query("gender")
        characterGender: String,
        @Query("page")
        filteredCharactersPage: Int
    ): Response<Root>
}