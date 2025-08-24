package com.example.merosathi.model;

import java.util.List;

public class SingleNotificationResponse {
    private boolean success;
    private String message;
    private Notification data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Notification getData() {
        return data;
    }
}
