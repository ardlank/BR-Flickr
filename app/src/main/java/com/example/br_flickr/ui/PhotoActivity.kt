package com.example.br_flickr.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.br_flickr.R
import com.example.br_flickr.model.Photo
import com.example.br_flickr.source.SourceConstants.FLICKR_DEFAULT_SEARCH
import com.example.br_flickr.ui.adapter.PhotoListAdapter
import com.example.br_flickr.util.GlideApp
import com.example.br_flickr.util.InjectorUtils
import com.example.br_flickr.util.MySuggestionProvider
import com.example.br_flickr.util.NetworkState
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.photo_main.*


class PhotoActivity : AppCompatActivity() {

    private val viewModel: PhotoViewModel by viewModels {
        InjectorUtils.providePhotoViewModelFactory(this)
    }

    private val keyQuery = "query"

    private lateinit var recyclerView: RecyclerView

    private lateinit var menuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_main)
        navigation.setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener)

        initAdapter()
        initSwipeToRefresh()
        val searchQuery = savedInstanceState?.getString(keyQuery) ?: FLICKR_DEFAULT_SEARCH
        fetchSearch(searchQuery)
    }

    private val OnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        clearScreen()
        when (item.itemId) {
            R.id.navigation_home -> {
                navigateToHome()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_bookmark -> {
                navigateToBookmarks()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun clearScreen() {
        (recyclerView.adapter as? PhotoListAdapter)?.submitList(null)
        recyclerView.scrollToPosition(0)
    }

    private fun navigateToHome() {
        viewModel.setRepo(isNetwork = true)
        menuItem.isVisible = true
        setSearchTitle()
    }

    private fun navigateToBookmarks() {
        viewModel.setRepo(isNetwork = false)
        menuItem.isVisible = false
        supportActionBar?.title = getString(R.string.bookmarks)
    }

    private fun initAdapter() {
        val glide = GlideApp.with(this)
        val adapter = PhotoListAdapter(glide, InjectorUtils.getFlickrDatabase(this)) {
            viewModel.retry()
        }
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.adapter = adapter
        viewModel.photos.observe(this, Observer<PagedList<Photo>> { posts ->
            adapter.submitList(posts)
        })
        viewModel.networkState.observe(this, Observer { networkState ->
            adapter.setNetworkState(networkState)
        })
    }

    private fun initSwipeToRefresh() {
        val swipeRefresh: SwipeRefreshLayout = findViewById(R.id.swipe_refresh)
        viewModel.refreshState.observe(this, Observer { networkState ->
            swipeRefresh.isRefreshing = networkState == NetworkState.LOADING
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
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(keyQuery, viewModel.currentSearch())
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

    private fun setSearchTitle() {
        if (viewModel.currentSearch() is String) {
            supportActionBar?.title = viewModel.currentSearch()
        }
    }

    private fun fetchSearch(query: String) {
        if(::menuItem.isInitialized) {
            menuItem.collapseActionView()
        }
        if (query.isNotEmpty()) {
            if (viewModel.showSearch(query)) {
                clearScreen()
                setSearchTitle()
            }
        }
    }
}
