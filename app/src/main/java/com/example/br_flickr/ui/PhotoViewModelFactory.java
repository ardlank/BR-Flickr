package com.example.br_flickr.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.br_flickr.source.local.FlickrDatabase;
import com.example.br_flickr.source.remote.FlickrApi;

public class PhotoViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static FlickrDatabase flickrDatabase;
    private static FlickrApi flickApi;

    public PhotoViewModelFactory(@NonNull FlickrApi flickrApi, FlickrDatabase flickrDatabase) {
        this.flickrDatabase = flickrDatabase;
        this.flickApi = flickrApi;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PhotoViewModel.class)) {
            return (T) new PhotoViewModel(flickApi, flickrDatabase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}