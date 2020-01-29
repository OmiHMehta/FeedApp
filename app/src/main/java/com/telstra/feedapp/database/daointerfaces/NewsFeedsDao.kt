package com.telstra.feedapp.database.daointerfaces

import androidx.lifecycle.LiveData
import androidx.room.*
import com.telstra.feedapp.database.constants.RoomConstants
import com.telstra.feedapp.models.NewsFeed

@Dao
interface NewsFeedsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(dataList: MutableList<NewsFeed>)

    @Query("SELECT * FROM ${RoomConstants.Tables.NewsFeed}")
    fun getFeedData(): LiveData<MutableList<NewsFeed>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateData(dataList: MutableList<NewsFeed>)

    @Query("DELETE FROM ${RoomConstants.Tables.NewsFeed}")
    fun deleteAllData()

}
