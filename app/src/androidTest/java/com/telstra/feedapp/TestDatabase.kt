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

    @get:Rule
    var testRule: ActivityTestRule<MainActivity>? =
        ActivityTestRule<MainActivity>(MainActivity::class.java)

    private lateinit var database: RoomDatabaseBuilder
    private var nFeedDao: NewsFeedsDao? = null

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
        tempData.add(NewsFeed(title = "Omi", description = "Technology Analyst"))
        tempData.add(NewsFeed(title = "Dhaval", description = "Investment Manager"))
        tempData.add(NewsFeed(title = "Harshal", description = "Flutter Developer"))
        tempData.add(NewsFeed(title = "Shreya", description = "IOS developer"))
        tempData.add(NewsFeed(title = "Hasmukh", description = "Android Developer"))

        //try {
        nFeedDao!!.insertData(tempData)
        /*} catch (e: Exception) {
            println("TAG --- $TAG --> ${e.message}")
        } finally {
            database.close()
        }*/
    }

    @Test
    fun testDeleteDataFromDataBase() {
        //try {
        nFeedDao?.deleteAllData()
        /*} catch (e: Exception) {
            println("TAG --- $TAG --> ${e.message}")
        } finally {
            database.close()
        }*/
    }

    @After
    fun tearDown() {
        nFeedDao = null
    }
}