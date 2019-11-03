package com.example.br_flickr.model;

import java.util.List;

public class PhotosResponse {

    private PhotoPage photos;

    public PhotoPage getPhotos() {
        return photos;
    }

    public void setPhotos(PhotoPage photos) {
        this.photos = photos;
    }

    public static class PhotoPage {

        private int page;
        private int pages;
        private int perpage;
        private String total;
        private List<PhotoObject> photo;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getPerpage() {
            return perpage;
        }

        public void setPerpage(int perpage) {
            this.perpage = perpage;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public List<PhotoObject> getPhoto() {
            return photo;
        }

        public void setPhoto(List<PhotoObject> photo) {
            this.photo = photo;
        }
    }
}