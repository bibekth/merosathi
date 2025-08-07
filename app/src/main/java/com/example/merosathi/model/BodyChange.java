package com.example.merosathi.model;

import java.util.ArrayList;

public class BodyChange {
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
        Integer id, user_id, week;
        String title, banner_image, description;
        Integer order;
        ArrayList<String> references;
        BabyGrowthUser user;

        public Integer getId() {
            return id;
        }

        public Integer getUser_id() {
            return user_id;
        }

        public Integer getWeek() {
            return week;
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

        public Integer getOrder() {
            return order;
        }

        public ArrayList<String> getReferences() {
            return references;
        }

        public BabyGrowthUser getUser() {
            return user;
        }

        public static class BabyGrowthUser {
            Integer id;
            String name, email;
            Doctor doctor;

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
