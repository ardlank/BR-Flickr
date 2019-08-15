package com.example.br_flickr.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.br_flickr.R;

public class CustomImageView extends ConstraintLayout {

    private ImageView photoImage;
    private ProgressBar progressbar;
    private Button retryButton;

    CustomImageView(Context context) {
        super(context);
        init();
    }

    CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    CustomImageView(Context context, AttributeSet attrs, Integer defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.custom_image_view, this);

        photoImage = findViewById(R.id.photo_image);
        progressbar = findViewById(R.id.spinner);
        retryButton = findViewById(R.id.retry_button);
    }

    public void clearAll() {
        progressbar.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
    }

    public void showProgress() {
        retryButton.setVisibility(View.GONE);
        progressbar.setVisibility(View.VISIBLE);
    }

    public void showRetry(){
        progressbar.setVisibility(View.GONE);
        retryButton.setVisibility(View.VISIBLE);
    }

    public Button getRetryButton() {
        return retryButton;
    }

    public ImageView getPhotoImage(){
        return photoImage;
    }

}
