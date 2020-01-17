package com.telstra.feedapp.networkadapter.api.request

import com.telstra.feedapp.networkadapter.apiconstants.ApiProvider
import com.telstra.feedapp.repositories.NewsFeedRepository
import io.reactivex.Observable
import retrofit2.http.POST

interface ApiInterface {

    @POST(ApiProvider.ApiGetNewsFeed)
    fun getNewsFeedsList(): Observable<NewsFeedRepository>

}