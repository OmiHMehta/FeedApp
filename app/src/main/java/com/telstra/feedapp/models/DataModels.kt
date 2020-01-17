package com.telstra.feedapp.models

import com.google.gson.annotations.SerializedName
import com.telstra.feedapp.networkadapter.apiconstants.ApiConstants

data class NewsFeed(

    @SerializedName(ApiConstants.Title)
    private val title: String? = "",

    @SerializedName(ApiConstants.Description)
    private val description: String? = "",

    @SerializedName(ApiConstants.ImageHref)
    private val imageUrl: String? = ""

) {
    fun getTitle() = title ?: "N/A"
    fun getDescription() = description ?: "N/A"
    fun getImageUrl() = imageUrl ?: ""
}