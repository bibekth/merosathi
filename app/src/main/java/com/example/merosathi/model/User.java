package com.example.merosathi.model;

import java.lang.reflect.Array;

public class User {
    Boolean success;
    String message;
    Data data;

    public User() {
    }

    public User(Boolean success, String message, Data data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        String token;

        public Data() {
        }

        public Data(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }
}
