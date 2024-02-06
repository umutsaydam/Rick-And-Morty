package com.umutsaydam.rickandmortyapp.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.rickandmortyapp.models.Root
import com.umutsaydam.rickandmortyapp.repository.RickAndMortyRepository
import com.umutsaydam.rickandmortyapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class CharactersViewModel(
    private val rickAndMortyRepository: RickAndMortyRepository,
) : ViewModel() {
    val characters: MutableLiveData<Resource<Root>> = MutableLiveData()
    var charactersPageNum = 1
    private var charactersRoot: Root? = null

    init {
        getCharacters()
    }

    fun getCharacters() = viewModelScope.launch {
        characters.postValue(null)
        characters.postValue(Resource.Loading())
        val response = rickAndMortyRepository.getCharacters(charactersPageNum)
        characters.postValue(handleCharactersResponse(response))
    }

    private fun handleCharactersResponse(response: Response<Root>): Resource<Root> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                charactersPageNum++
                if (charactersRoot == null) {
                    charactersRoot = resultResponse
                } else {
                    val oldCharacters = charactersRoot?.results
                    val newCharacters = resultResponse.results
                    oldCharacters?.addAll(newCharacters)
                }
                return Resource.Success(charactersRoot ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


}