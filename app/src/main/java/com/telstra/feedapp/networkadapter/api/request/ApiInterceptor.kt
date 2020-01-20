package com.telstra.feedapp.networkadapter.api.request

import com.google.gson.JsonObject
import com.telstra.feedapp.networkadapter.api.response.ApiResponse
import com.telstra.feedapp.repositories.NewsFeedRepository
import io.reactivex.disposables.Disposable

interface ApiInterceptor {

    fun getNewsFeed(
        parameters: JsonObject = JsonObject(), apiResponse: ApiResponse<NewsFeedRepository>? = null
    ): Disposable

}