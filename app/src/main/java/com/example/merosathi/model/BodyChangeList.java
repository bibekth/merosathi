package com.example.merosathi.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BodyChangeList {
    Boolean success;
    String message;
    ArrayList<Data> data;

    public BodyChangeList() {
    }

    public BodyChangeList(Boolean success, String message, ArrayList<Data> data) {
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

    public ArrayList<Data> getData() {
        return data;
    }

    public static class Data {
        Integer id, user_id;
        String title, banner_image, description, order;
        Array references;
        BabyGrowth.Data.BabyGrowthUser user;

        public Data() {
        }

        public Data(Integer id, Integer user_id, String title, String banner_image, String description, String order, Array references) {
            this.id = id;
            this.user_id = user_id;
            this.title = title;
            this.banner_image = banner_image;
            this.description = description;
            this.order = order;
            this.references = references;
        }

        public Data(Integer id, Integer user_id, String title, String banner_image, String description, String order, Array references, BabyGrowth.Data.BabyGrowthUser user) {
            this.id = id;
            this.user_id = user_id;
            this.title = title;
            this.banner_image = banner_image;
            this.description = description;
            this.order = order;
            this.references = references;
            this.user = user;
        }

        public Integer getId() {
            return id;
        }

        public Integer getUser_id() {
            return user_id;
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

        public String getOrder() {
            return order;
        }

        public Array getReferences() {
            return references;
        }

        public BabyGrowth.Data.BabyGrowthUser getUser() {
            return user;
        }

        public static class BabyGrowthUser {
            Integer id;
            String name, email;
            Doctor doctor;

            public BabyGrowthUser() {
            }

            public BabyGrowthUser(Integer id, String name, String email, Doctor doctor) {
                this.id = id;
                this.name = name;
                this.email = email;
                this.doctor = doctor;
            }

            public Integer getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getEmail() {
                return email;
            }

            public Doctor getDoctor() {
                return doctor;
            }
            public static class Doctor {
                Integer id, user_id;
                String name, email, contact, description, address;

                public Doctor() {
                }

                public Doctor(Integer id, Integer user_id, String name, String email, String contact, String description, String address) {
                    this.id = id;
                    this.user_id = user_id;
                    this.name = name;
                    this.email = email;
                    this.contact = contact;
                    this.description = description;
                    this.address = address;
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

                public String getEmail() {
                    return email;
                }

                public String getContact() {
                    return contact;
                }

                public String getDescription() {
                    return description;
                }

                public String getAddress() {
                    return address;
                }
            }
        }
    }
}
