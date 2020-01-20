package com.telstra.feedapp.ui.feeds.view

import com.telstra.feedapp.repositories.NewsFeedRepository

interface FeedView {
    fun onDataFetched(newsRepo: NewsFeedRepository)
    fun onFailed(apiTag: String, message: String = "")
}