package com.telstra.feedapp

import com.telstra.feedapp.models.NewsFeed
import com.telstra.feedapp.repositories.NewsFeedRepository
import com.telstra.feedapp.ui.feeds.presenter.FeedPresenter
import com.telstra.feedapp.ui.feeds.view.FeedView
import org.junit.Before
import org.junit.Test

class FeedPresenterTest : FeedView {

    private val TAG: String = FeedPresenterTest::class.java.simpleName

    private lateinit var view: FeedView
    private lateinit var presenter: FeedPresenter

    override fun onDataFetched(newsRepo: NewsFeedRepository) {
        val dataList: MutableList<NewsFeed> = mutableListOf()
        dataList.addAll(newsRepo.getDataList())
        println("TAG --- $TAG --> ${dataList.size}")
    }

    override fun onFailed(apiTag: String, message: String) =
        println("TAG --- $TAG --> $apiTag -- $message")

    @Before
    fun initTest() {
        presenter = FeedPresenter(this)
    }

    @Test
    fun testApiCall() {
        presenter.refreshNewsFeedList()
    }
}