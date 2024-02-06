package com.umutsaydam.rickandmortyapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umutsaydam.rickandmortyapp.R
import com.umutsaydam.rickandmortyapp.adapter.CharacterAdapter
import com.umutsaydam.rickandmortyapp.databinding.FragmentFilterBinding
import com.umutsaydam.rickandmortyapp.db.CharacterDatabase
import com.umutsaydam.rickandmortyapp.models.Result
import com.umutsaydam.rickandmortyapp.repository.RickAndMortyRepository
import com.umutsaydam.rickandmortyapp.ui.viewModels.CharactersViewModelFactory
import com.umutsaydam.rickandmortyapp.ui.viewModels.FilterCharactersViewModel
import com.umutsaydam.rickandmortyapp.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.umutsaydam.rickandmortyapp.utils.Constants.Companion.SEARCH_FILTER_TIME_DELAY
import com.umutsaydam.rickandmortyapp.utils.ItemClickListener
import com.umutsaydam.rickandmortyapp.utils.NetworkConnectivityChecker
import com.umutsaydam.rickandmortyapp.utils.Resource
import com.umutsaydam.rickandmortyapp.utils.ScrollHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FilterFragment : Fragment() {
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private var filterName = ""
    private var filterGender = ""
    private var filterStatus = ""
    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var viewModel: FilterCharactersViewModel
    private var job: Job? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        characterAdapter = CharacterAdapter()
        binding.ivBackImg.setOnClickListener { findNavController().popBackStack() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (NetworkConnectivityChecker.isInternetConnected(activity!!.applicationContext)) {
            initViewModel()
            setupSpinners()
            binding.etFilterCharacterName.addTextChangedListener { editable ->
                if (editable != null) {
                    filterName = editable.toString().trim().lowercase()
                    getFilteredCharacters()
                }
            }
        } else {
            binding.etFilterCharacterName.isEnabled = false
            showNoConnectionMessage()
        }

        binding.fbToHomeFilter.setOnClickListener {
            val layoutManager = binding.rcFilteredCharacters.layoutManager as GridLayoutManager
            layoutManager.smoothScrollToPosition(binding.rcFilteredCharacters, null, 0)
        }
    }

    private fun showNoConnectionMessage() {
        binding.ivNoConnection.visibility = View.VISIBLE
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            CharactersViewModelFactory(RickAndMortyRepository(CharacterDatabase(activity!!.applicationContext)))
        )[FilterCharactersViewModel::class.java]

        setupRecycler()
        viewModel.filteredCharacters.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hiderProgressBar()
                    response.data?.let { characters ->
                        characterAdapter.differ.submitList(characters.results)
                        val totalPages = characters.results.size / QUERY_PAGE_SIZE
                        isLastPage = viewModel.filteredCharactersPage == totalPages
                        if (isLastPage) {
                            binding.rcFilteredCharacters.setPadding(0, 0, 0, 0)
                        }
                    }
                }

                is Resource.Error -> {
                    hiderProgressBar()
                    response.message?.let { message ->

                    }
                }

                is Resource.Loading -> {
                    showProgressBar()

                }
            }
        }
    }

    private fun showProgressBar() {
        binding.filterCharactersPaginationProgressBar.visibility = View.VISIBLE
    }

    private fun hiderProgressBar() {
        binding.filterCharactersPaginationProgressBar.visibility = View.GONE
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
                val view = activity!!.currentFocus
                view?.let {
                    val imm =
                        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
            ScrollHelper.scrollHelper(recyclerView, binding.fbToHomeFilter)
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
                viewModel.getFilteredCharacters(filterName, filterStatus, "", "", filterGender)
                isScrolling = false
            }
        }
    }

    private fun setupSpinners() {
        val genderSpinner = binding.spinnerCharacterGender
        setupSpinner(genderSpinner, R.array.genders) { position ->
            val selectedGender = genderSpinner.getItemAtPosition(position).toString()
            filterGender = if (position > 0) selectedGender else ""
            binding.rcFilteredCharacters.layoutManager!!.removeAllViews()
            getFilteredCharacters()
        }

        val statusSpinner = binding.spinnerCharacterStatus
        setupSpinner(statusSpinner, R.array.status) { position ->
            val selectedStatus = statusSpinner.getItemAtPosition(position).toString()
            filterStatus = if (position > 0) selectedStatus else ""
            getFilteredCharacters()
        }
    }

    private fun getFilteredCharacters() {
        viewModel.resetFilteredCharactersRoot()
        binding.rcFilteredCharacters.layoutManager!!.removeAllViews()
        characterAdapter.differ.submitList(null)
        job?.cancel()
        job = MainScope().launch {
            delay(SEARCH_FILTER_TIME_DELAY)
            viewModel.getFilteredCharacters(filterName, filterStatus, "", "", filterGender)
        }
    }

    private val itemClickListener = object : ItemClickListener {
        override fun onItemClickedListener(character: Result) {
            val bundle = Bundle()
            bundle.putSerializable("characterDetail", character)
            findNavController().navigate(
                R.id.action_filterFragment_to_characterDetailFragment,
                bundle
            )
        }
    }

    private fun setupRecycler() {
        characterAdapter = CharacterAdapter()
        characterAdapter.setItemClickListener(itemClickListener)
        characterAdapter.setHasStableIds(true)
        binding.rcFilteredCharacters.apply {
            adapter = characterAdapter
            layoutManager = GridLayoutManager(activity, 2)
            addOnScrollListener(this@FilterFragment.scrollListener)
        }
    }

    // onItemSelected: (Int) -> Unit is a lambda expression
    private fun setupSpinner(spinner: Spinner, arrayId: Int, onItemSelected: (Int) -> Unit) {
        spinner.adapter = ArrayAdapter.createFromResource(
            requireContext(),
            arrayId,
            R.layout.filter_list_item
        )

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                p1: View?,
                position: Int,
                p3: Long,
            ) {
                onItemSelected(position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}