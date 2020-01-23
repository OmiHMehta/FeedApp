package com.telstra.feedapp

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.telstra.feedapp.ui.feeds.MainActivity
import com.telstra.feedapp.ui.feeds.view.FeedFragment
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
open class FeedFragmentTest {

    private val TAG: String = FeedFragmentTest::javaClass.name

    private var activity: MainActivity? = null

    @get:Rule
    var testRule: ActivityTestRule<MainActivity>? =
        ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Before
    fun setUp() = testRule?.let { activity = it.activity }

    @Test
    fun fragmentLaunchTest() {
        activity?.run {
            val feedFragment = FeedFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, feedFragment, getString(R.string.app_name))
                .commit()

            InstrumentationRegistry.getInstrumentation().waitForIdleSync()

            val swipeRefreshLayout: SwipeRefreshLayout? =
                feedFragment.view?.findViewById(R.id.slSwipeRefreshLayout)
            swipeRefreshLayout?.let { println("TAG --- $TAG --> Test Passed Successfully") }
        }
    }

    @After
    fun tearDown() {
        activity = null
        testRule = null
    }
}