package com.telstra.feedapp.repositories

import com.google.gson.annotations.SerializedName
import com.telstra.feedapp.models.NewsFeed
import com.telstra.feedapp.networkadapter.api.response.ApiStatus
import com.telstra.feedapp.networkadapter.apiconstants.ApiConstants

class NewsFeedRepository : ApiStatus() {

    @SerializedName(ApiConstants.Title)
    private val title: String? = null

    @SerializedName(ApiConstants.Rows)
    private val dataList: MutableList<NewsFeed>? = mutableListOf()

    fun getTitle() = title ?: "N/A"

    fun getDataList(): MutableList<NewsFeed> = dataList ?: mutableListOf()
}