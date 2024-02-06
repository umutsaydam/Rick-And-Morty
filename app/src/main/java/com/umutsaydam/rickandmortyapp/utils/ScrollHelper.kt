package com.umutsaydam.rickandmortyapp.utils

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

object ScrollHelper {
    fun scrollHelper(recyclerView: RecyclerView, fbToHome: FloatingActionButton) {
        val layoutManager = recyclerView.layoutManager as GridLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (firstVisibleItemPosition >= 4) {
            fbToHome.visibility = View.VISIBLE
            fbToHome.animate().apply {
                alpha(1f)
                duration = 300
            }.withEndAction {
                fbToHome.animate().apply {
                    scaleX(1.1f)
                    scaleY(1.1f)
                }
            }
        } else {
            fbToHome.animate().apply {
                duration = 300
                scaleX(.0f)
                scaleY(.0f)
                alpha(.0f)
            }.withEndAction {
                fbToHome.visibility = View.GONE
            }
        }
    }
}