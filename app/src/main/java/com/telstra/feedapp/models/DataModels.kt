package com.telstra.feedapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.telstra.feedapp.database.constants.RoomConstants
import com.telstra.feedapp.networkadapter.apiconstants.ApiConstants

@Entity(tableName = RoomConstants.Tables.NewsFeed)
data class NewsFeed(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @SerializedName(ApiConstants.Title)
    private val title: String? = "",

    @SerializedName(ApiConstants.Description)
    private val description: String? = "",

    @SerializedName(ApiConstants.ImageHref)
    @ColumnInfo(name = RoomConstants.ColumnName.ImageUrl)
    private val imageUrl: String? = ""

) {
    fun getTitle() = title ?: "N/A"
    fun getDescription() = description ?: "N/A"
    fun getImageUrl() = imageUrl ?: ""
}