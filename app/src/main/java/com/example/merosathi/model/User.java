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
        String token, name, email;
        Integer id;

        Person person;

        public Data() {
        }

        public Data(String token) {
            this.token = token;
        }

        public Data(String name, String email, Integer id, Person person) {
            this.name = name;
            this.email = email;
            this.id = id;
            this.person = person;
        }

        public String getToken() {
            return token;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public Integer getId() {
            return id;
        }

        public Person getPerson() {
            return person;
        }

        public static class Person {
            Integer id, user_id;
            String name, username, contact, email, dob, lmp, expected_date;

            public Person() {
            }

            public Person(Integer id, Integer user_id, String name, String username, String contact, String email, String dob, String lmp, String expected_date) {
                this.id = id;
                this.user_id = user_id;
                this.name = name;
                this.username = username;
                this.contact = contact;
                this.email = email;
                this.dob = dob;
                this.lmp = lmp;
                this.expected_date = expected_date;
            }

            public Integer getId() {
                return id;
            }

            public Integer getUser_id() {
                return user_id;
            }

            public String getName() {
                return name;
            }

            public String getUsername() {
                return username;
            }

            public String getContact() {
                return contact;
            }

            public String getEmail() {
                return email;
            }

            public String getDob() {
                return dob;
            }

            public String getLmp() {
                return lmp;
            }

            public String getExpected_date() {
                return expected_date;
            }
        }
    }
}
