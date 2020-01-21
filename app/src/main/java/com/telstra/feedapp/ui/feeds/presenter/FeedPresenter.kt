package com.telstra.feedapp.ui.feeds.presenter

import com.telstra.feedapp.FeedApp
import com.telstra.feedapp.adapters.NewsFeedAdapter
import com.telstra.feedapp.models.NewsFeed
import com.telstra.feedapp.networkadapter.api.request.ApiInterceptor
import com.telstra.feedapp.networkadapter.api.response.ApiResponse
import com.telstra.feedapp.repositories.NewsFeedRepository
import com.telstra.feedapp.ui.feeds.view.FeedView

open class FeedPresenter(private val view: FeedView) {

    private val TAG: String = FeedPresenter::class.java.simpleName

    private val apiClient: ApiInterceptor = FeedApp.getApiClient()

    private val adapter: NewsFeedAdapter
    private val feedList: MutableList<NewsFeed> = mutableListOf()

    init {
        adapter = NewsFeedAdapter(feedList)
    }

    private fun getNewsFeedList() =
        apiClient.getNewsFeed(apiResponse = object : ApiResponse<NewsFeedRepository> {

            // TODO : Api Callback
            override fun onSuccess(
                apiTag: String, message: String, apiResponse: NewsFeedRepository
            ) {
                feedList.addAll(apiResponse.getDataList())
                adapter.notifyDataSetChanged()
                view.onDataFetched(apiResponse)
            }

            override fun onComplete(apiTag: String, message: String) {
            }

            override fun onError(apiTag: String, message: String) = view.onFailed(apiTag, message)

            override fun onError(apiTag: String, throwable: Throwable) =
                view.onFailed(apiTag, throwable.message ?: "")
        })

    fun getAdapter() =
        adapter

    fun refreshNewsFeedList() {
        feedList.clear()
        adapter.notifyDataSetChanged()
        getNewsFeedList()
    }

    fun getFeedDataFromPosition(position: Int): String =
        if ((position >= 0) && (feedList.size > 0) && (position < feedList.size)) feedList[position].getTitle()
        else ""
}