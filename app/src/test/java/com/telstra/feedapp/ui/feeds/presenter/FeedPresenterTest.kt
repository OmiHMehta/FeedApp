package com.telstra.feedapp.ui.feeds.presenter

import android.content.Context
import com.telstra.feedapp.networkadapter.api.request.ApiInterceptor
import com.telstra.feedapp.networkadapter.api.response.ApiResponse
import com.telstra.feedapp.networkadapter.apiconstants.ApiProvider
import com.telstra.feedapp.repositories.NewsFeedRepository
import com.telstra.feedapp.ui.feeds.view.FeedView
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.Executor

@RunWith(MockitoJUnitRunner.Silent::class)
open class FeedPresenterTest {

    private val TAG: String = FeedPresenterTest::class.java.simpleName

    private lateinit var presenter: FeedPresenter

    private val response: NewsFeedRepository = NewsFeedRepository()

    @Mock
    private lateinit var context: Context

    @Mock
    lateinit var view: FeedView

    @Mock
    internal lateinit var apiClient: ApiInterceptor

    @Captor
    private lateinit var argumentCaptor: ArgumentCaptor<ApiResponse<NewsFeedRepository>>

    private val scheduler = object : Scheduler() {
        override fun createWorker(): Worker =
            ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true)
    }

    @Before
    fun setUp() {
        RxJavaPlugins.setInitIoSchedulerHandler { scheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler }

        presenter = FeedPresenter.getInstance(context, apiClient, view)
    }

    @Test
    fun testSuccessfulApiCall() {
        makeApiCall()

        // TODO : Test Successful response
        argumentCaptor.value.onSuccess(
            ApiProvider.ApiGetNewsFeed, "argumentCaptor : onSuccess called", response
        )
        Mockito.verify(view).onDataFetched(response)
        Assert.assertEquals(response, response)

        // TODO : Test Failing response
        argumentCaptor.value.onError(
            ApiProvider.ApiGetNewsFeed, "argumentCaptor : onError called"
        )
        Mockito.verify(view).onFailed(
            ApiProvider.ApiGetNewsFeed, "argumentCaptor : onError called"
        )
        Assert.assertEquals(
            "argumentCaptor : onError called", "argumentCaptor : onError called"
        )
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    private fun makeApiCall() {
        presenter.getNewsFeedList()
        Mockito.verify(apiClient).getNewsFeed(argumentCaptor.capture())
    }
}