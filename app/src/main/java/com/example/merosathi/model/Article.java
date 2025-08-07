package com.example.merosathi.model;

import java.util.ArrayList;

public class Article {
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
        Integer id, user_id;
        String title, banner_image, description;
        ArrayList<String> references; // ✅ Fixed
        ArrayList<Section> sections;
        ArticleUser user;

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

        public ArrayList<String> getReferences() {
            return references;
        }

        public ArrayList<Section> getSections() {
            return sections;
        }

        public ArticleUser getUser() {
            return user;
        }

        public static class Section {
            Integer id, article_id;
            String title, description;

            public Integer getId() {
                return id;
            }

            public Integer getArticle_id() {
                return article_id;
            }

            public String getTitle() {
                return title;
            }

            public String getDescription() {
                return description;
            }
        }

        public static class ArticleUser {
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
                Integer id;
                String name, email, contact, address, description;

                public Integer getId() {
                    return id;
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

                public String getAddress() {
                    return address;
                }

                public String getDescription() {
                    return description;
                }
            }
        }
    }
}
