package com.telstra.feedapp.database.roomrepository

import androidx.lifecycle.LiveData
import com.telstra.feedapp.database.daointerfaces.NewsFeedsDao
import com.telstra.feedapp.models.NewsFeed

class FeedRepository(private val nFeedDao: NewsFeedsDao) {

    val newsDataList: LiveData<MutableList<NewsFeed>> = nFeedDao.getFeedData()

    fun insertOrUpdateData(dataList: MutableList<NewsFeed>, isUpdate: Boolean = false) =
        if (!isUpdate) nFeedDao.insertData(dataList)
        else nFeedDao.updateData(dataList)

    fun deleteAllData() = nFeedDao.deleteAllData()

}