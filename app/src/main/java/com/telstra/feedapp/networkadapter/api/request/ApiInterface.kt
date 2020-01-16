package com.telstra.feedapp.networkadapter.api.request

import com.telstra.feedapp.networkadapter.apiconstants.ApiProvider
import retrofit2.http.POST

interface ApiInterface {

    @POST(ApiProvider.ApiGetNewsFeed)
    fun getNewsFeeds()

}