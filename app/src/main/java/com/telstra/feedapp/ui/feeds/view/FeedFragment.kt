package com.telstra.feedapp.ui.feeds.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.telstra.feedapp.R
import com.telstra.feedapp.repositories.NewsFeedRepository
import com.telstra.feedapp.ui.feeds.presenter.FeedPresenter
import com.telstra.feedapp.utility.NetworkProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.concurrent.TimeUnit

class FeedFragment : Fragment(), FeedView {

    private val presenter: FeedPresenter = FeedPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_feed, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        // TODO : Once UI is loaded fetch data from API
        val disposable =
            Observable.timer(650, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe { getData() }
    }

    override fun onDataFetched(newsRepo: NewsFeedRepository) {
        slSwipeRefreshLayout.isRefreshing = false
        activity!!.tvTitle.text = newsRepo.getTitle()
    }

    override fun onFailed(apiTag: String, message: String) {
        slSwipeRefreshLayout.isRefreshing = false
        context?.let { Toast.makeText(it, message, Toast.LENGTH_LONG).show() }
    }

    private fun initViews() {
        activity!!.ivRefresh.setOnClickListener { getData() }

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
                activity!!.tvTitle.text = title
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