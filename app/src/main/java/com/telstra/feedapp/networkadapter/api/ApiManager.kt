package com.telstra.feedapp.networkadapter.api

import android.content.Context
import com.google.gson.JsonObject
import com.telstra.feedapp.networkadapter.api.request.ApiInterceptor
import com.telstra.feedapp.networkadapter.api.request.ApiInterface
import com.telstra.feedapp.networkadapter.api.response.ApiResponse
import com.telstra.feedapp.networkadapter.apiconstants.ApiProvider
import com.telstra.feedapp.networkadapter.retrofit.RetrofitClient
import com.telstra.feedapp.repositories.NewsFeedRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ApiManager(context: Context) : ApiInterceptor {

    private val TAG: String = ApiManager::class.java.simpleName

    private var apiInterface: ApiInterface = RetrofitClient.getInstance(context).apiClient

    companion object {

        private var apiManager: ApiInterceptor? = null

        fun getInstance(context: Context): ApiInterceptor {
            if (apiManager == null)
                apiManager = ApiManager(context)
            return apiManager!!
        }
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
                apiResponse?.onError(ApiProvider.ApiGetNewsFeed, it)
            }, {
                apiResponse?.onComplete(ApiProvider.ApiGetNewsFeed, "")
            })
}