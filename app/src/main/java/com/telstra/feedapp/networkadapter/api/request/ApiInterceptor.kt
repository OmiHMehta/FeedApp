package com.telstra.feedapp.networkadapter.api.request

import com.telstra.feedapp.networkadapter.api.response.ApiResponse
import com.telstra.feedapp.repositories.NewsFeedRepository
import io.reactivex.disposables.Disposable

interface ApiInterceptor {

    fun getNewsFeed(apiResponse: ApiResponse<NewsFeedRepository>? = null): Disposable

}