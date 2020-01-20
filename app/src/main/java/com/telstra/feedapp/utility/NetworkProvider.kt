package com.telstra.feedapp.utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object NetworkProvider {

    fun isConnected(context: Context): Boolean {
        val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworks: NetworkInfo? = connectivityManager.activeNetworkInfo
        return (activeNetworks != null) && (activeNetworks.isConnected)
    }
}