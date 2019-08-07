package com.example.br_flickr.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.br_flickr.model.Photo
import com.example.br_flickr.util.MySuggestionProvider
import com.example.br_flickr.util.NetworkState
import com.example.br_flickr.util.GlideApp
import com.example.br_flickr.R
import com.example.br_flickr.ui.adapter.PhotoListAdapter

//Search/Main View
class PhotoSearchActivity : AppCompatActivity() {

    private lateinit var viewModel: PhotoSearchViewModel

    private lateinit var recyclerView: RecyclerView

    private lateinit var menuItem: MenuItem

    val defaultSearch = "people"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_search)

        viewModel = ViewModelProviders.of(this).get(PhotoSearchViewModel::class.java)

        initAdapter()
        initSwipeToRefresh()
    }

    private fun initAdapter() {
        val glide = GlideApp.with(this)
        val adapter = PhotoListAdapter(glide)
        {
            viewModel.retry()
        }
        recyclerView = findViewById(R.id.recyclerView)
        //recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
        viewModel.photos.observe(this, Observer<PagedList<Photo>> {
            adapter.submitList(it)
        })
        viewModel.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })
    }

    private fun initSwipeToRefresh() {
        val swipeRefresh: SwipeRefreshLayout = findViewById(R.id.swipe_refresh)
        viewModel.refreshState.observe(this, Observer {
            swipeRefresh.isRefreshing = it == NetworkState.LOADING
        })
        swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the options menu from XML
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        menuItem = menu.findItem(R.id.search)
        (menuItem.actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(
                searchManager.getSearchableInfo(componentName)
            )
            setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
        }
        fetchSearch(defaultSearch)
        return true
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (Intent.ACTION_SEARCH == intent!!.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { input ->
                val query = input.trim()
                fetchSearch(query)
                SearchRecentSuggestions(this, MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)
                    .saveRecentQuery(query, null)
            }
        }
    }

    private fun fetchSearch(query: String) {
        supportActionBar?.title = query
        menuItem.collapseActionView()
        if (query.isNotEmpty()) {
            if (viewModel.showSearch(query)) {
                recyclerView.scrollToPosition(0)
                (recyclerView.adapter as? PhotoListAdapter)?.submitList(null)
            }
        }
    }
}
