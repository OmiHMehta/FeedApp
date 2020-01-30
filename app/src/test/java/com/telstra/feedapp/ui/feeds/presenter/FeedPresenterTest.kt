package com.telstra.feedapp.ui.feeds.presenter

import android.content.Context
import com.telstra.feedapp.database.daointerfaces.NewsFeedsDao
import com.telstra.feedapp.database.room.RoomDatabaseBuilder
import com.telstra.feedapp.models.NewsFeed
import com.telstra.feedapp.networkadapter.api.ApiManager
import com.telstra.feedapp.networkadapter.api.request.ApiInterceptor
import com.telstra.feedapp.networkadapter.api.response.ApiResponse
import com.telstra.feedapp.repositories.NewsFeedRepository
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.Executor

@RunWith(MockitoJUnitRunner.Silent::class)
open class FeedPresenterTest {

    private val TAG: String = FeedPresenterTest::class.java.simpleName

    private lateinit var apiClient: ApiInterceptor

    @Mock
    private lateinit var context: Context

    @Mock
    lateinit var nFeedDao: NewsFeedsDao

    private val scheduler = object : Scheduler() {
        override fun createWorker(): Worker =
            ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true)
    }

    @Before
    fun setUp() {

        RxJavaPlugins.setInitIoSchedulerHandler { scheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler }

        apiClient = ApiManager(context)

        val database = RoomDatabaseBuilder.getInstance(context)
        nFeedDao = database.getNewsFeedDao()
    }

    @Test
    fun testApiCall() {
        Mockito.`when`(apiClient.getNewsFeed(apiResponse = object :
            ApiResponse<NewsFeedRepository> {
            override fun onSuccess(
                apiTag: String, message: String, apiResponse: NewsFeedRepository
            ) {
                println("TAG --- $TAG --> ${apiResponse.getDataList().size} - Api Called")
            }

            override fun onComplete(apiTag: String, message: String) {
            }

            override fun onError(apiTag: String, message: String) {
                println("TAG --- $TAG --> $message")
            }

            override fun onError(apiTag: String, throwable: Throwable) {
                println("TAG --- $TAG --> ${throwable.message}")
            }
        })).then {}
    }

    @Test
    fun testDataBase() {
        val tempData: MutableList<NewsFeed> = mutableListOf()
        tempData.add(NewsFeed(title = "Omi", description = "Technology Analyst"))
        tempData.add(NewsFeed(title = "Dhaval", description = "Investment Manager"))
        tempData.add(NewsFeed(title = "Harshal", description = "Flutter Developer"))
        tempData.add(NewsFeed(title = "Shreya", description = "IOS developer"))
        tempData.add(NewsFeed(title = "Hasmukh", description = "Android Developer"))

        nFeedDao.insertData(tempData)
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }
}