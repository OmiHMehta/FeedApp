package com.telstra.feedapp.ui.feeds

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.telstra.feedapp.R
import com.telstra.feedapp.ui.feeds.view.FeedFragment
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {

    private val TAG_FEED_FRAGMENT: String = FeedFragment::class.java.simpleName

    private var feedFragment: FeedFragment = FeedFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val fTransition = supportFragmentManager.beginTransaction()
        fTransition.add(R.id.fragmentContainer, feedFragment, TAG_FEED_FRAGMENT).commit()
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
