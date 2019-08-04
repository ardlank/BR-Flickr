package com.example.br_flickr.Util

import android.app.ListActivity
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.widget.Toast

class SearchableActivity : ListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        handleIntent(intent)
    }

    public override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent(intent)
    }



    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            doSearch(query)
        }/*
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                SearchRecentSuggestions(this, MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)
                    .saveRecentQuery(query, null)
                doSearch(query)
            }
        }*/
    }

    private fun doSearch(queryStr: String) {
        Toast.makeText(applicationContext, queryStr, Toast.LENGTH_LONG).show()
    }

}