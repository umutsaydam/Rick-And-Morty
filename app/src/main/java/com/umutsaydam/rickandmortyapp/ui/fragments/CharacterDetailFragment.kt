package com.umutsaydam.rickandmortyapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.umutsaydam.rickandmortyapp.R
import com.umutsaydam.rickandmortyapp.databinding.FragmentCharacterDetailBinding
import com.umutsaydam.rickandmortyapp.db.CharacterDatabase
import com.umutsaydam.rickandmortyapp.models.Result
import com.umutsaydam.rickandmortyapp.repository.RickAndMortyRepository
import com.umutsaydam.rickandmortyapp.ui.viewModels.CharactersViewModelFactory
import com.umutsaydam.rickandmortyapp.ui.viewModels.FavoritesViewModel

class CharacterDetailFragment : Fragment() {
    private var _binding: FragmentCharacterDetailBinding? = null
    private val binding get() = _binding!!
    private val args: CharacterDetailFragmentArgs by navArgs()
    private var character: Result? = null
    private lateinit var viewModel: FavoritesViewModel
    private var isThereCharacter = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)
        character = args.characterDetail
        fillInfo()

        binding.ivBackImg.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            CharactersViewModelFactory(RickAndMortyRepository(CharacterDatabase(context!!)))
        )[FavoritesViewModel::class.java]

        character?.let {
            viewModel.getSingleCharacter(it.id)
            viewModel.isThereCharacter.observe(viewLifecycleOwner) { id ->
                isThereCharacter = id
                setIvFavorite(isThereCharacter(it.id))
            }
        }

        binding.ivFavorite.setOnClickListener {
            character?.let { character ->
                val state = isThereCharacter(character.id)
                if (state) {
                    viewModel.deleteCharacter(character)
                    Snackbar.make(
                        view,
                        "Deleted character from favorites list.",
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("Undo") {
                            viewModel.saveCharacter(character)
                        }
                        .show()
                } else {
                    viewModel.saveCharacter(character)
                    Snackbar.make(view, "Added to your favorites", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            viewModel.deleteCharacter(character)
                        }
                        .show()
                }
                setIvFavorite(state)
            }
        }
    }

    private fun isThereCharacter(id: Int): Boolean {
        return isThereCharacter != 0
    }

    private fun setIvFavorite(state: Boolean) {
        binding.ivFavorite.setImageDrawable(
            ContextCompat.getDrawable(
                context!!,
                if (state)
                    R.drawable.ic_saved
                else
                    R.drawable.ic_save
            )
        )
    }

    private fun fillInfo() {
        character?.let { detail ->
            Glide.with(binding.root.context).load(detail.image).into(binding.ivCharacterDetailImage)
            binding.tvCharacterDetailName.text = detail.name
            binding.tvCharacterDetailSpecies.text = detail.species
            binding.tvCharacterDetailStatus.text = detail.status
            binding.ivCharacterDetailGender.setImageResource(
                if (detail.gender == "Male") R.drawable.ic_male
                else R.drawable.ic_female
            )
            binding.tvCharacterDetailType.text = detail.type
            binding.tvCharacterDetailOriginName.text = detail.origin.name
            binding.tvCharacterDetailOriginName.text = detail.location.name
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}