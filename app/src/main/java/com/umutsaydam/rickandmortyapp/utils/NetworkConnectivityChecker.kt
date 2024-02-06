package com.umutsaydam.rickandmortyapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


object NetworkConnectivityChecker {
    fun isInternetConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        network?.let {
            return connectivityManager.getNetworkCapabilities(network)!!.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_VALIDATED
            )
        }
        return false
    }
}