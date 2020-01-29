package com.telstra.feedapp.ui.feeds

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.telstra.feedapp.R
import com.telstra.feedapp.ui.feeds.view.FeedFragment
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {

    private val TAG_FEED_FRAGMENT: String = FeedFragment::class.java.simpleName

    private lateinit var feedFragment: FeedFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        feedFragment = if (savedInstanceState == null) FeedFragment()
        else supportFragmentManager.findFragmentByTag(TAG_FEED_FRAGMENT) as FeedFragment

        if (!feedFragment.isAdded) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, feedFragment, TAG_FEED_FRAGMENT).commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("fragment_launch_status", true)
        super.onSaveInstanceState(outState)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        when (newConfig.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
            }
        }
    }
}
