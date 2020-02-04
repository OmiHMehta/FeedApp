package com.telstra.feedapp.ui.feeds.presenter

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.telstra.feedapp.database.room.RoomDatabaseBuilder
import com.telstra.feedapp.database.roomrepository.FeedRepository
import com.telstra.feedapp.models.NewsFeed
import com.telstra.feedapp.networkadapter.api.request.ApiInterceptor
import com.telstra.feedapp.networkadapter.api.response.ApiResponse
import com.telstra.feedapp.repositories.NewsFeedRepository
import com.telstra.feedapp.ui.feeds.view.FeedView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class FeedPresenter(private val view: FeedView) {

    private val TAG: String = FeedPresenter::class.java.simpleName

    private val KEY_RECYCLERVIEW_STATE = "key_recyclerview_state"

    // private val apiClient: ApiInterceptor = ApiManager()
    private lateinit var apiClient: ApiInterceptor

    private var recyclerViewState: Parcelable? = null

    private val liveFeedList: LiveData<MutableList<NewsFeed>>

    companion object {

        private var INSTANCE: FeedPresenter? = null
        private lateinit var repository: FeedRepository

        fun getInstance(
            context: Context, apiClient: ApiInterceptor, view: FeedView
        ): FeedPresenter =
            INSTANCE ?: synchronized(this) {
                val database = RoomDatabaseBuilder.getInstance(context)
                repository = FeedRepository(database.getNewsFeedDao())

                INSTANCE = FeedPresenter(view)
                INSTANCE!!.apiClient = apiClient
                INSTANCE!!
            }
    }

    init {
        liveFeedList = repository.newsDataList
    }

    // TODO : API call
    fun getNewsFeedList() =
        apiClient.getNewsFeed(apiResponse = object : ApiResponse<NewsFeedRepository> {

            // TODO : Api Callback
            override fun onSuccess(
                apiTag: String, message: String, apiResponse: NewsFeedRepository
            ) {
                println("TAG --- $TAG --> $message")
                view.onDataFetched(apiResponse)
                if (apiResponse.getDataList().isNotEmpty()) {
                    val disposable =
                        Observable.fromCallable {
                            repository.insertOrUpdateData(
                                apiResponse.getDataList(), liveFeedList.value?.isNotEmpty() ?: false
                            )
                        }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                {},
                                {
                                    println("TAG --- $TAG -- db_operation --> ${it.message}")
                                },
                                {
                                    view.onDataFetched(apiResponse)
                                })
                }
            }

            override fun onComplete(apiTag: String, message: String) {
                println("TAG --- $TAG --> $message")
            }

            override fun onError(apiTag: String, message: String) {
                println("TAG --- $TAG --> $message")
                view.onFailed(apiTag, message)
            }

            override fun onError(apiTag: String, throwable: Throwable) =
                view.onFailed(apiTag, throwable.message ?: "")
        })

    fun getLiveData(): LiveData<MutableList<NewsFeed>> = liveFeedList

    fun getFeedDataFromPosition(position: Int): String =
        if ((position >= 0) && (liveFeedList.value!!.isNotEmpty()) &&
            (position < liveFeedList.value!!.size)
        ) liveFeedList.value!![position].getTitle()
        else ""

    // TODO : Save data when config change occurs
    fun onSaveInstanceState(recyclerView: RecyclerView, outState: Bundle) =
        recyclerView.layoutManager?.run {
            recyclerViewState = onSaveInstanceState()
            outState.putParcelable(KEY_RECYCLERVIEW_STATE, recyclerViewState)
        }

    // TODO : Restore data and View State after config change
    fun getRecyclerViewState(savedInstanceState: Bundle?): Parcelable? {
        recyclerViewState = savedInstanceState?.getParcelable(KEY_RECYCLERVIEW_STATE)
        return recyclerViewState
    }
}