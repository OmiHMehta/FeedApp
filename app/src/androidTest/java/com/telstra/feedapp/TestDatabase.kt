package com.telstra.feedapp

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.telstra.feedapp.database.daointerfaces.NewsFeedsDao
import com.telstra.feedapp.database.room.RoomDatabaseBuilder
import com.telstra.feedapp.models.NewsFeed
import com.telstra.feedapp.ui.feeds.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class TestDatabase {

    private val TAG: String = TestDatabase::class.java.simpleName

    private var activity: MainActivity? = null

    private lateinit var database: RoomDatabaseBuilder
    private var nFeedDao: NewsFeedsDao? = null

    @get:Rule
    var testRule: ActivityTestRule<MainActivity>? =
        ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Before
    fun setUp() {
        testRule?.let {
            activity = it.activity
            database = RoomDatabaseBuilder.getInMemoryDatabase(activity!!)
            nFeedDao = database.getNewsFeedDao()
        }
    }

    @Test
    fun testInsertDataIntoDataBase() {
        val tempData: MutableList<NewsFeed> = mutableListOf()
        tempData.add(NewsFeed(id = 1, title = "Omi", description = "Technology Analyst"))
        tempData.add(NewsFeed(id = 2, title = "Dhaval", description = "Investment Manager"))
        tempData.add(NewsFeed(id = 3, title = "Harshal", description = "Flutter Developer"))
        tempData.add(NewsFeed(id = 4, title = "Shreya", description = "IOS developer"))
        tempData.add(NewsFeed(id = 5, title = "Hasmukh", description = "Android Developer"))

        nFeedDao!!.insertData(tempData)
    }

    @After
    fun tearDown() {
        nFeedDao = null
    }
}