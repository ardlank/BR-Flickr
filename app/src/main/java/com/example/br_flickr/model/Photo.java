package com.example.br_flickr.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName="photos", indices = {@Index(value = {"id"},
        unique = true)})
public class Photo {
    @NonNull
    @PrimaryKey
    private String id;
    private String owner;
    private String secret;
    private String title;
    private String url_sq;
    private String url_t;
    private String url_s;
    private String url_q;
    private String url_m;
    private String url_n;
    private String url_z;
    private String url_c;
    private String url_l;
    private String url_o;
    private int indexInResponse;
    private boolean isBookmarked = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl_sq() {
        return url_sq;
    }

    public void setUrl_sq(String url_sq) {
        this.url_sq = url_sq;
    }

    public String getUrl_t() {
        return url_t;
    }

    public void setUrl_t(String url_t) {
        this.url_t = url_t;
    }

    public String getUrl_s() {
        return url_s;
    }

    public void setUrl_s(String url_s) {
        this.url_s = url_s;
    }

    public String getUrl_q() {
        return url_q;
    }

    public void setUrl_q(String url_q) {
        this.url_q = url_q;
    }

    public String getUrl_m() {
        return url_m;
    }

    public void setUrl_m(String url_m) {
        this.url_m = url_m;
    }

    public String getUrl_n() {
        return url_n;
    }

    public void setUrl_n(String url_n) {
        this.url_n = url_n;
    }

    public String getUrl_z() {
        return url_z;
    }

    public void setUrl_z(String url_z) {
        this.url_z = url_z;
    }

    public String getUrl_c() {
        return url_c;
    }

    public void setUrl_c(String url_c) {
        this.url_c = url_c;
    }

    public String getUrl_l() {
        return url_l;
    }

    public void setUrl_l(String url_l) {
        this.url_l = url_l;
    }

    public String getUrl_o() {
        return url_o;
    }

    public void setUrl_o(String url_o) {
        this.url_o = url_o;
    }

    public Integer getIndexInResponse() {
        return indexInResponse;
    }

    public void setIndexInResponse(Integer indexInResponse) {
        this.indexInResponse = indexInResponse;
    }

    public Boolean getIsBookmarked() {
        return isBookmarked;
    }

    public void setIsBookmarked(Boolean isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Photo)) {
            return false;
        }

        Photo photo = (Photo) o;
        return photo.id.equals(id) && photo.title.equals(title);
    }
}