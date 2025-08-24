package com.example.merosathi.model;

import java.util.List;

public class NotificationResponse {
    private boolean success;
    private String message;
    private List<Notification> data;  // <-- data is a list in your API

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<Notification> getData() {  // return the list directly
        return data;
    }
}
