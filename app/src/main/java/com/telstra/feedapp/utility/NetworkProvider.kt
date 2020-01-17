package com.telstra.feedapp.utility

import android.content.Context
import android.net.ConnectivityManager

object NetworkProvider {

    fun isConnected(context: Context): Boolean {
        val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo.isConnected
    }
}