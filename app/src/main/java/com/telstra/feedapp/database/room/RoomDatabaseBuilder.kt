package com.telstra.feedapp.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.telstra.feedapp.database.constants.RoomConstants
import com.telstra.feedapp.database.daointerfaces.NewsFeedsDao
import com.telstra.feedapp.models.NewsFeed

@Database(
    entities = [NewsFeed::class], version = RoomConstants.RoomBuilder.DB_VERSION,
    exportSchema = false
)
abstract class RoomDatabaseBuilder : RoomDatabase() {

    abstract fun getNewsFeedDao(): NewsFeedsDao

    companion object {
        private var INSTANCE: RoomDatabaseBuilder? = null

        fun getInstance(context: Context): RoomDatabaseBuilder =
            INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context, RoomDatabaseBuilder::class.java, RoomConstants.RoomBuilder.DB_NAME
                ).build()
                INSTANCE!!
            }
    }
}