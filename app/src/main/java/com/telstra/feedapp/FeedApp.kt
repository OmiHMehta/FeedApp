package com.telstra.feedapp

import android.app.Application
import java.io.File

class FeedApp : Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this

        //apiManager = ApiManager()
    }

    companion object {

        private lateinit var instance: Application
        //private lateinit var apiManager: ApiInterceptor

        fun getInstance() = instance
        //fun getApiClient(): ApiInterceptor = apiManager
    }
}