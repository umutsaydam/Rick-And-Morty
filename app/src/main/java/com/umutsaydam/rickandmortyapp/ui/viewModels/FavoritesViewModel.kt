package com.umutsaydam.rickandmortyapp.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.rickandmortyapp.models.Result
import com.umutsaydam.rickandmortyapp.repository.RickAndMortyRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(private val rickAndMortyRepository: RickAndMortyRepository) : ViewModel() {
    var isThereCharacter: LiveData<Int> = MutableLiveData()

    fun saveCharacter(character: Result) = viewModelScope.launch {
        rickAndMortyRepository.upsert(character)
    }

    fun getAllCharacters() = rickAndMortyRepository.getAllCharacters()

    fun getSingleCharacter(id: Int) = viewModelScope.launch {
        isThereCharacter = rickAndMortyRepository.getSingleCharacter(id)
    }

    fun deleteCharacter(character: Result) = viewModelScope.launch {
        rickAndMortyRepository.deleteCharacter(character)
    }

}