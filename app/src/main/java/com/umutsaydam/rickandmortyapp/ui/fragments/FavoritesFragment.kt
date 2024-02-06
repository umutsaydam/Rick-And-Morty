package com.umutsaydam.rickandmortyapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.umutsaydam.rickandmortyapp.R
import com.umutsaydam.rickandmortyapp.adapter.CharacterAdapter
import com.umutsaydam.rickandmortyapp.databinding.FragmentFavoritesBinding
import com.umutsaydam.rickandmortyapp.db.CharacterDatabase
import com.umutsaydam.rickandmortyapp.models.Result
import com.umutsaydam.rickandmortyapp.repository.RickAndMortyRepository
import com.umutsaydam.rickandmortyapp.ui.viewModels.CharactersViewModelFactory
import com.umutsaydam.rickandmortyapp.ui.viewModels.FavoritesViewModel
import com.umutsaydam.rickandmortyapp.utils.ItemClickListener

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FavoritesViewModel
    private lateinit var characterAdapter: CharacterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            CharactersViewModelFactory(RickAndMortyRepository(CharacterDatabase(context!!)))
        )[FavoritesViewModel::class.java]

        setupRecyclerView()
        viewModel.getAllCharacters().observe(viewLifecycleOwner) { favCharactersList ->
            characterAdapter.differ.submitList(favCharactersList)
        }
    }

    private val itemClickListener = object : ItemClickListener {
        override fun onItemClickedListener(character: Result) {
            val bundle = Bundle()
            bundle.putSerializable("characterDetail", character)
            findNavController().navigate(
                R.id.action_favoritesFragment_to_characterDetailFragment,
                bundle
            )
        }

    }

    private fun setupRecyclerView() {
        characterAdapter = CharacterAdapter()
        characterAdapter.setItemClickListener(itemClickListener)
        characterAdapter.setHasStableIds(true)
        binding.favoritesRcCharacters.apply {
            adapter = characterAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}