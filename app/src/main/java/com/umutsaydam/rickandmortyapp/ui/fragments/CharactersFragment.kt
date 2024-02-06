package com.umutsaydam.rickandmortyapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umutsaydam.rickandmortyapp.R
import com.umutsaydam.rickandmortyapp.adapter.CharacterAdapter
import com.umutsaydam.rickandmortyapp.databinding.FragmentCharactersBinding
import com.umutsaydam.rickandmortyapp.db.CharacterDatabase
import com.umutsaydam.rickandmortyapp.models.Result
import com.umutsaydam.rickandmortyapp.repository.RickAndMortyRepository
import com.umutsaydam.rickandmortyapp.ui.viewModels.CharactersViewModel
import com.umutsaydam.rickandmortyapp.ui.viewModels.CharactersViewModelFactory
import com.umutsaydam.rickandmortyapp.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.umutsaydam.rickandmortyapp.utils.ItemClickListener
import com.umutsaydam.rickandmortyapp.utils.NetworkConnectivityChecker
import com.umutsaydam.rickandmortyapp.utils.Resource
import com.umutsaydam.rickandmortyapp.utils.ScrollHelper

class CharactersFragment : Fragment() {
    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CharactersViewModel
    private lateinit var characterAdapter: CharacterAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (NetworkConnectivityChecker.isInternetConnected(activity!!.applicationContext))
            initViewModel()
        else
            showNoConnectionMessage()

        binding.ivFilter.setOnClickListener { visitFilterFragment() }
        binding.fbToHome.setOnClickListener {
            val layoutManager = binding.rcCharacters.layoutManager as GridLayoutManager
            layoutManager.smoothScrollToPosition(binding.rcCharacters, null, 0)
        }
    }

    private fun showNoConnectionMessage() {
        binding.ivNoConnection.visibility = View.VISIBLE
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            CharactersViewModelFactory(RickAndMortyRepository(CharacterDatabase(activity!!.applicationContext)))
        )[CharactersViewModel::class.java]

        setupRecyclerView()
        viewModel.characters.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { characters ->
                        characterAdapter.differ.submitList(characters.results)
                        val totalPages = characters.results.size / QUERY_PAGE_SIZE

                        isLastPage = viewModel.charactersPageNum == totalPages
                        if (isLastPage) {
                            binding.rcCharacters.setPadding(0, 0, 0, 0)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun visitFilterFragment() {
        findNavController().navigate(R.id.action_charactersFragment_to_filterFragment)
    }

    private fun hideProgressBar() {
        binding.charactersPaginationProgressBar.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.charactersPaginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }

            ScrollHelper.scrollHelper(recyclerView, binding.fbToHome)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling

            if (shouldPaginate && NetworkConnectivityChecker.isInternetConnected(activity!!.applicationContext)) {
                viewModel.getCharacters()
                isScrolling = false
            }
        }
    }

    private val itemClickListener = object : ItemClickListener {
        override fun onItemClickedListener(character: Result) {
            val bundle = Bundle()
            bundle.putSerializable("characterDetail", character)
            findNavController().navigate(
                R.id.action_charactersFragment_to_characterDetailFragment,
                bundle
            )
        }

    }

    private fun setupRecyclerView() {
        characterAdapter = CharacterAdapter()
        characterAdapter.setItemClickListener(itemClickListener)
        characterAdapter.setHasStableIds(true)
        binding.rcCharacters.apply {
            adapter = characterAdapter
            layoutManager = GridLayoutManager(activity, 2)
            addOnScrollListener(this@CharactersFragment.scrollListener)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}