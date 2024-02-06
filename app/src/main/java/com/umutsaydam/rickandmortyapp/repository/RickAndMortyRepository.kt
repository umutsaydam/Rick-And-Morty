package com.umutsaydam.rickandmortyapp.repository

import com.umutsaydam.rickandmortyapp.api.RetrofitInstance
import com.umutsaydam.rickandmortyapp.db.CharacterDatabase
import com.umutsaydam.rickandmortyapp.models.Result

class RickAndMortyRepository(val db: CharacterDatabase) {
    suspend fun getCharacters(charactersPage: Int) =
        RetrofitInstance.api.getCharacters(charactersPage)

    suspend fun upsert(character: Result) = db.getCharacterDao().upsert(character)

    fun getAllCharacters() = db.getCharacterDao().getAllCharacters()

    suspend fun getFilteredCharacters(
        characterName: String,
        characterStatus: String,
        characterSpecies: String,
        characterType: String,
        characterGender: String,
        filteredCharactersPage: Int
    ) = RetrofitInstance.api.getFilteredCharacters(
        characterName,
        characterStatus,
        characterSpecies,
        characterType,
        characterGender,
        filteredCharactersPage
    )

    fun getSingleCharacter(id: Int) = db.getCharacterDao().getSingleCharacter(id)

    suspend fun deleteCharacter(character: Result) = db.getCharacterDao().deleteCharacter(character)
}