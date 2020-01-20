package com.telstra.feedapp

import android.app.Application
import com.telstra.feedapp.networkadapter.api.ApiManager
import com.telstra.feedapp.networkadapter.api.request.ApiInterceptor

class FeedApp : Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this
        apiManager = ApiManager()
    }

    companion object {

        private lateinit var instance:FeedApp
        private lateinit var apiManager: ApiInterceptor

        fun getInstance() = instance
        fun getApiClient() = apiManager
    }
}