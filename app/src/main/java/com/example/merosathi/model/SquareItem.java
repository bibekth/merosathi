package com.example.merosathi.model;

public class SquareItem {
    Integer id;
    String title;
    String bannerImage, type;

    public SquareItem(Integer id, String title, String bannerImage, String type) {
        this.id = id;
        this.title = title;
        this.bannerImage = bannerImage;
        this.type = type;
    }

    public SquareItem(String title, String bannerImage, String type) {
        this.title = title;
        this.bannerImage = bannerImage;
        this.type = type;
    }

    public SquareItem(String title, String bannerImage) {
        this.title = title;
        this.bannerImage = bannerImage;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public String getType() {
        return type;
    }
}
