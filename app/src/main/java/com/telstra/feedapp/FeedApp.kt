package com.telstra.feedapp

import android.app.Application
import com.telstra.feedapp.networkadapter.api.ApiManager
import com.telstra.feedapp.networkadapter.api.request.ApiInterceptor

class FeedApp : Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this

        // apiManager = ApiManager()
        apiManager = ApiManager.getInstance(instance)
    }

    companion object {

        private lateinit var instance: Application
        private lateinit var apiManager: ApiInterceptor

        fun getInstance() = instance
        fun getApiClient(): ApiInterceptor = apiManager
    }
}