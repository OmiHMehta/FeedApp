package com.telstra.feedapp.ui.feeds.view

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.telstra.feedapp.R
import com.telstra.feedapp.adapters.NewsFeedAdapter
import com.telstra.feedapp.repositories.NewsFeedRepository
import com.telstra.feedapp.ui.feeds.presenter.FeedPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.concurrent.TimeUnit

class FeedFragment : Fragment(), FeedView {

    private val KEY_FRAGMENT_STATE = "key_fragment_state"
    private val KEY_RECYCLERVIEW_STATE = "key_recyclerview_state"

    private var recyclerViewState: Parcelable? = null

    private lateinit var presenter: FeedPresenter

    private lateinit var adapter: NewsFeedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        presenter = FeedPresenter.getInstance(context!!, this)
        adapter = NewsFeedAdapter(presenter.getData())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_feed, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        // TODO : Once UI is loaded fetch data from API and Restore View State
        if (savedInstanceState == null)
            Observable.timer(650, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { getData() }
        else {
            recyclerViewState = savedInstanceState.getParcelable(KEY_RECYCLERVIEW_STATE)
            rvNewsFeedList.layoutManager!!.onRestoreInstanceState(recyclerViewState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_FRAGMENT_STATE, "launched")
        rvNewsFeedList.layoutManager?.run {
            recyclerViewState = onSaveInstanceState()
            outState.putParcelable(KEY_RECYCLERVIEW_STATE, recyclerViewState)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onDataFetched(newsRepo: NewsFeedRepository) {
        slSwipeRefreshLayout.isRefreshing = false
        activity!!.tvTitle.text = newsRepo.getTitle()
        adapter.notifyDataSetChanged()
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

        rvNewsFeedList.isSaveEnabled = true
        rvNewsFeedList.adapter = adapter
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

            adapter.notifyDataSetChanged()

            Observable.timer(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe { presenter.getNewsFeedList() }
        }
    }
}