package com.umutsaydam.rickandmortyapp.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.rickandmortyapp.models.Root
import com.umutsaydam.rickandmortyapp.repository.RickAndMortyRepository
import com.umutsaydam.rickandmortyapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class FilterCharactersViewModel(
    private val rickAndMortyRepository: RickAndMortyRepository,
) : ViewModel() {
    private var charactersRoot: Root? = null
    val filteredCharacters: MutableLiveData<Resource<Root>> = MutableLiveData()
    private var isFiltered = false
    private var filteredCharactersRoot: Root? = null
    var filteredCharactersPage = 1

    init {
        getFilteredCharacters("", "", "", "", "")
    }

    private fun handleFilteredCharactersResponse(response: Response<Root>): Resource<Root> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                filteredCharactersPage++
                if (filteredCharactersRoot == null) {
                    filteredCharactersRoot = resultResponse
                } else {
                    val oldCharacters = filteredCharactersRoot?.results
                    val newCharacters = resultResponse.results
                    oldCharacters?.addAll(newCharacters)
                }
                return Resource.Success(filteredCharactersRoot ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getFilteredCharacters(
        characterName: String,
        characterStatus: String,
        characterSpecies: String,
        characterType: String,
        characterGender: String,
    ) = viewModelScope.launch {
        filteredCharacters.postValue(Resource.Loading())
        val response = rickAndMortyRepository.getFilteredCharacters(
            characterName,
            characterStatus,
            characterSpecies,
            characterType,
            characterGender,
            filteredCharactersPage
        )
        isFiltered = true
        filteredCharacters.postValue(handleFilteredCharactersResponse(response))
    }

    fun resetFilteredCharactersRoot() {
        filteredCharactersRoot = null
        filteredCharactersPage = 1
    }
}