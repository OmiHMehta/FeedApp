package com.telstra.feedapp.networkadapter.api.request

import com.telstra.feedapp.networkadapter.apiconstants.ApiProvider
import com.telstra.feedapp.repositories.NewsFeedRepository
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("{url}")
    fun getNewsFeedsList(@Path("url") apiUrl: String = ApiProvider.ApiGetNewsFeed): Observable<NewsFeedRepository>

}