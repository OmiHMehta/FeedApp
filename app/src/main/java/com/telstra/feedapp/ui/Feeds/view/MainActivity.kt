package com.telstra.feedapp.ui.Feeds.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.telstra.feedapp.R
import com.telstra.feedapp.repositories.NewsFeedRepository
import com.telstra.feedapp.ui.Feeds.presenter.FeedPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), FeedView {

    private val presenter: FeedPresenter = FeedPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        initViews()

        val disposable =
            Observable.timer(650, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe { getData() }
    }

    override fun onDataFetched(newsRepo: NewsFeedRepository) {
        slSwipeRefreshLayout.isRefreshing = false
        toolBar.tvTitle.text = newsRepo.getTitle()
    }

    override fun onFailed(apiTag: String, message: String) {
        slSwipeRefreshLayout.isRefreshing = false
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun initViews() {
        ivRefresh.setOnClickListener { getData() }

        // TODO : Setting up SwipeRefreshLayout
        slSwipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_blue_bright, android.R.color.holo_green_light,
            android.R.color.holo_orange_light, android.R.color.holo_red_light
        )
        slSwipeRefreshLayout.setOnRefreshListener { getData() }

        rvNewsFeedList.adapter = presenter.getAdapter()
        rvNewsFeedList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager: LinearLayoutManager =
                    rvNewsFeedList.layoutManager as LinearLayoutManager
                val title =
                    presenter.getFeedDataFromPosition(layoutManager.findFirstCompletelyVisibleItemPosition())
                toolBar.tvTitle.text = title
            }
        })
    }

    // TODO : Call api show data in List
    private fun getData() {
        slSwipeRefreshLayout.takeIf { !it.isRefreshing }.run {
            slSwipeRefreshLayout.isRefreshing = true
            presenter.refreshNewsFeedList()
        }
    }
}
