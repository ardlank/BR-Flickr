package com.example.br_flickr.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.br_flickr.source.local.FlickrDatabase;

public class PhotoSearchViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static FlickrDatabase flickrDatabase;

    public PhotoSearchViewModelFactory(@NonNull FlickrDatabase flickrDatabase) {
        this.flickrDatabase = flickrDatabase;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PhotoSearchViewModel.class)) {
            return (T) new PhotoSearchViewModel(flickrDatabase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
