package com.telstra.feedapp

import android.app.Application
import com.telstra.feedapp.networkadapter.api.ApiManager
import com.telstra.feedapp.networkadapter.api.request.ApiInterceptor

class FeedApp : Application() {

    override fun onCreate() {
        super.onCreate()

        apiManager = ApiManager(this)
    }

    companion object {
        private lateinit var apiManager: ApiInterceptor
        fun getApiClient() = apiManager
    }
}