package com.telstra.feedapp.ui.feeds.presenter

import android.content.Context
import com.telstra.feedapp.models.NewsFeed
import com.telstra.feedapp.networkadapter.api.ApiManager
import com.telstra.feedapp.networkadapter.api.request.ApiInterceptor
import com.telstra.feedapp.networkadapter.api.response.ApiResponse
import com.telstra.feedapp.repositories.NewsFeedRepository
import com.telstra.feedapp.ui.feeds.view.FeedView

open class FeedPresenter(private val view: FeedView) {

    private val feedList: MutableList<NewsFeed> = mutableListOf()

    companion object {
        private var INSTANCE: FeedPresenter? = null
        private lateinit var apiClient: ApiInterceptor
        fun getInstance(context: Context, view: FeedView): FeedPresenter {
            if (INSTANCE == null) {
                INSTANCE = FeedPresenter(view)
                apiClient = ApiManager(context)
            }
            return INSTANCE!!
        }
    }

    // TODO : API call
    fun getNewsFeedList() =
        apiClient.getNewsFeed(apiResponse = object : ApiResponse<NewsFeedRepository> {

            // TODO : Api Callback
            override fun onSuccess(
                apiTag: String, message: String, apiResponse: NewsFeedRepository
            ) {
                feedList.addAll(apiResponse.getDataList())
                view.onDataFetched(apiResponse)
            }

            override fun onComplete(apiTag: String, message: String) {
            }

            override fun onError(apiTag: String, message: String) = view.onFailed(apiTag, message)

            override fun onError(apiTag: String, throwable: Throwable) =
                view.onFailed(apiTag, throwable.message ?: "")
        })

    fun getData(): MutableList<NewsFeed> = feedList

    fun getFeedDataFromPosition(position: Int): String =
        if ((position >= 0) && (feedList.size > 0) && (position < feedList.size)) feedList[position].getTitle()
        else ""
}