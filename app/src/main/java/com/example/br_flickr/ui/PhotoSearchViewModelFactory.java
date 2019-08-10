package com.example.br_flickr.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.br_flickr.source.local.FlickrDB;

public class PhotoSearchViewModelFactory implements ViewModelProvider.Factory {
    private FlickrDB flickrDB;


    public PhotoSearchViewModelFactory(FlickrDB flickrDB) {
        this.flickrDB = flickrDB;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PhotoSearchViewModel.class)) {
            return (T) new PhotoSearchViewModel(flickrDB);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
