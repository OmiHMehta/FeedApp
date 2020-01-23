package com.telstra.feedapp.ui.feeds.presenter

import android.content.Context
import com.telstra.feedapp.networkadapter.api.ApiManager
import com.telstra.feedapp.ui.feeds.view.FeedView
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

    @Mock
    private lateinit var context: Context

    @Mock
    lateinit var view: FeedView

    @Mock
    lateinit var presenter: FeedPresenter

    private val scheduler = object : Scheduler() {
        override fun createWorker(): Worker =
            ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true)
    }

    @Before
    fun setUp() {

        RxJavaPlugins.setInitIoSchedulerHandler { scheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler }

        presenter = FeedPresenter(ApiManager.getInstance(context), view)
    }

    @Test
    fun testApiCall() {

        Mockito.`when`(presenter.getNewsFeedList()).thenAnswer {

        }

    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }
}