package com.umutsaydam.rickandmortyapp.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.umutsaydam.rickandmortyapp.repository.RickAndMortyRepository

class CharactersViewModelFactory(private val rickAndMortyRepository: RickAndMortyRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(FilterCharactersViewModel::class.java) -> {
                FilterCharactersViewModel(rickAndMortyRepository) as T
            }
            modelClass.isAssignableFrom(FavoritesViewModel::class.java) -> {
                FavoritesViewModel(rickAndMortyRepository) as T
            }
            modelClass.isAssignableFrom(CharactersViewModel::class.java) -> {
                CharactersViewModel(rickAndMortyRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
        }
    }
}

