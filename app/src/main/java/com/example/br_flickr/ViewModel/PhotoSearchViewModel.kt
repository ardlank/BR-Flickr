package com.example.br_flickr.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.br_flickr.Source.Remote.ApiFactory
import com.example.br_flickr.Model.Photo
import com.example.br_flickr.Model.PhotosResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PhotoSearchViewModel: ViewModel() {

    private val TAG = "PhotoSearchViewModel"

    private val job = SupervisorJob()

    private val coroutineContext = Dispatchers.IO + job

    private val fickerApi = ApiFactory.fickerApi

    private val _photos = MutableLiveData<List<Photo>>()

    val photos : LiveData<List<Photo>>
    get() = _photos

    fun fetchSearch(query: String) {
        viewModelScope.launch(coroutineContext) {
            val response = fickerApi.search(query)
            try {
                if (response.isSuccessful) {
                    postPhotoData(response.body())
                } else {
                    Log.d("PhotoSearchActivity ", response.errorBody().toString())
                }

            } catch (e: Exception) {
                Log.e(TAG, e.message, e)
            }
        }
    }

    fun postPhotoData(photoResponse: PhotosResponse?) {
        if (photoResponse == null) return
        _photos.postValue(photoResponse.photos.photo)
    }

}