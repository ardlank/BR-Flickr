package com.example.br_flickr.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.br_flickr.R
import com.example.br_flickr.model.Photo
import com.example.br_flickr.source.Posts
import com.example.br_flickr.source.local.FlickrDB
import com.example.br_flickr.ui.adapter.PhotoListAdapter
import com.example.br_flickr.util.GlideApp
import com.example.br_flickr.util.MySuggestionProvider
import com.example.br_flickr.util.NetworkState
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.flickr_main.*
import com.google.gson.Gson



class FlickrActivity : AppCompatActivity() {

    private lateinit var viewModel: PhotoSearchViewModel

    private lateinit var recyclerView: RecyclerView

    private lateinit var menuItem: MenuItem

    private lateinit var flickrDB: FlickrDB

    val defaultSearch = "people"

    private val OnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                clearClear()
                viewModel.setRepo(isNetwork = true)
                menuItem.isVisible = true
                fetchSearch(viewModel.currentSearch()!!)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_bookmark -> {
                clearClear()
                viewModel.setRepo(isNetwork = false)
                menuItem.isVisible = false
                setTitle("Bookmarks")
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun clearClear() {
        recyclerView.scrollToPosition(0)
        (recyclerView.adapter as? PhotoListAdapter)?.submitList(null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flickr_main)
        navigation.setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener)

        flickrDB = FlickrDB.create(this)

        viewModel = ViewModelProviders.of(this, PhotoSearchViewModelFactory(flickrDB)).get(PhotoSearchViewModel::class.java)

        initAdapter()
        initSwipeToRefresh()
    }

    private fun initAdapter() {
        val glide = GlideApp.with(this)
        val adapter = PhotoListAdapter(glide, flickrDB)
        {
            viewModel.retry()
        }
        recyclerView = findViewById(R.id.recyclerView)
        //recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
        viewModel.photos.observe(this, Observer<PagedList<Photo>> { posts ->
            adapter.submitList(posts)
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
        if (Intent.ACTION_SEARCH == intent?.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { input ->
                val query = input.trim()
                fetchSearch(query)
                SearchRecentSuggestions(this, MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)
                    .saveRecentQuery(query, null)
            }
        }
    }

    private fun setTitle(title: String) {
        supportActionBar?.title = title
    }

    private fun fetchSearch(query: String) {
        setTitle(query)
        menuItem.collapseActionView()
        if (query.isNotEmpty()) {
            if (viewModel.showSearch(query)) {
                recyclerView.scrollToPosition(0)
                (recyclerView.adapter as? PhotoListAdapter)?.submitList(null)
            }
        }
    }
}
