package com.telstra.feedapp.networkadapter.api

import android.content.Context
import com.google.gson.JsonObject
import com.telstra.feedapp.networkadapter.api.request.ApiInterceptor
import com.telstra.feedapp.networkadapter.api.request.ApiInterface
import com.telstra.feedapp.networkadapter.api.response.ApiResponse
import com.telstra.feedapp.networkadapter.apiconstants.ApiProvider
import com.telstra.feedapp.networkadapter.retrofit.RetrofitClient
import com.telstra.feedapp.repositories.NewsFeedRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ApiManager() : ApiInterceptor {

    private val TAG: String = ApiManager::class.java.simpleName

    private var apiInterface: ApiInterface

    companion object {

        private var apiManager: ApiInterceptor? = null
        private var context: Context? = null

        fun getInstance(context: Context): ApiInterceptor {
            if (apiManager == null) {
                this.context = context
                apiManager = ApiManager()
            }
            return apiManager!!
        }
    }

    init {
        apiInterface = RetrofitClient(context!!).apiClient
    }

    override fun getNewsFeed(
        parameters: JsonObject, apiResponse: ApiResponse<NewsFeedRepository>?
    ): Disposable = apiInterface.getNewsFeedsList()
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            // TODO : Get Response Here
            apiResponse?.onSuccess(ApiProvider.ApiGetNewsFeed, "", it)
        }, {
            // TODO : Got Some error
            it?.let { println("TAG --- $TAG --> ${it.message}") }
            apiResponse?.onError(ApiProvider.ApiGetNewsFeed, it)
        }, {
            apiResponse?.onComplete(ApiProvider.ApiGetNewsFeed, "")
        })
}