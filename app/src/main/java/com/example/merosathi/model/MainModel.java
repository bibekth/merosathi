package com.example.merosathi.model;

public class MainModel {
    Boolean success;
    String message;
    Data data;

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
        BodyChange.Data body_change;
        BabyGrowth.Data baby_growth;
        String time;

        public BodyChange.Data getBody_change() {
            return body_change;
        }

        public BabyGrowth.Data getBaby_growth() {
            return baby_growth;
        }

        public String getTime() {
            return time;
        }


    }
}
