package com.umutsaydam.rickandmortyapp.utils

import com.umutsaydam.rickandmortyapp.models.Result

interface ItemClickListener {
    fun onItemClickedListener(character: Result)
}