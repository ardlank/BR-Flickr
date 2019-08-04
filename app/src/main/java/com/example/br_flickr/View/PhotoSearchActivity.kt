package com.example.br_flickr.View

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.br_flickr.Model.Photo
import com.example.br_flickr.ViewModel.PhotoSearchViewModel
import com.example.br_flickr.R


class PhotoSearchActivity : AppCompatActivity() {

    private lateinit var viewModel: PhotoSearchViewModel

    private lateinit var photoTileAdapter: PhotoTileAdapter

    private lateinit var recyclerView: RecyclerView

    val photosObserver = Observer<List<Photo>> { photos ->
        photoTileAdapter = PhotoTileAdapter(photos)
        recyclerView.adapter = photoTileAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        //viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(PhotoSearchViewModel::class.java)

        viewModel = ViewModelProviders.of(this).get(PhotoSearchViewModel::class.java)


        viewModel.photos.observe(this, photosObserver)
        fetchSearch()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the options menu from XML
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
        }

        return true
    }

    private fun fetchSearch() {
        viewModel.fetchSearch("People")
    }
}
