package com.example.merosathi.model;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ArticleList {
    Boolean success;
    String message;
    ArrayList<Data> data;

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public static class Data {
        Integer id;
        String title, banner_image, description;

        public Data() {
        }

        public Data(Integer id, String title, String banner_image, String description) {
            this.id = id;
            this.title = title;
            this.banner_image = banner_image;
            this.description = description;
        }

        public Integer getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getBanner_image() {
            return banner_image;
        }

        public String getDescription() {
            return description;
        }
    }
}
