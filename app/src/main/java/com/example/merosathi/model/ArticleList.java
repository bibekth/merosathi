package com.example.merosathi.model;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ArticleList {
    Boolean success;
    String message;
    ArrayList<Data> data;

    public ArticleList() {
    }

    public ArticleList(Boolean success, String message, ArrayList<Data> data) {
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
        String title, banner_image, description;
        Array references;
        ArrayList<Article.Data.Sections> sections;
        Article.Data.ArticleUser user;

        public Data() {
        }

        public Data(Integer id, Integer user_id, String title, String banner_image, String description, Array references) {
            this.id = id;
            this.user_id = user_id;
            this.title = title;
            this.banner_image = banner_image;
            this.description = description;
            this.references = references;
        }

        public Data(Integer id, Integer user_id, String title, String banner_image, String description, Array references, ArrayList<Article.Data.Sections> sections, Article.Data.ArticleUser user) {
            this.id = id;
            this.user_id = user_id;
            this.title = title;
            this.banner_image = banner_image;
            this.description = description;
            this.references = references;
            this.sections = sections;
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

        public Array getReferences() {
            return references;
        }

        public ArrayList<Article.Data.Sections> getSections() {
            return sections;
        }

        public Article.Data.ArticleUser getUser() {
            return user;
        }

        public static class Sections {
            Integer id, article_id;
            String title, description;

            public Sections() {
            }

            public Sections(Integer id, Integer article_id, String title, String description) {
                this.id = id;
                this.article_id = article_id;
                this.title = title;
                this.description = description;
            }

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
            Article.Data.ArticleUser.Doctor doctor;

            public ArticleUser() {
            }

            public ArticleUser(Integer id, String name, String email, Article.Data.ArticleUser.Doctor doctor) {
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

            public Article.Data.ArticleUser.Doctor getDoctor() {
                return doctor;
            }

            public static class Doctor {
                Integer id;
                String name, email, contact, address, description;

                public Doctor() {
                }

                public Doctor(Integer id, String name, String email, String contact, String address, String description) {
                    this.id = id;
                    this.name = name;
                    this.email = email;
                    this.contact = contact;
                    this.address = address;
                    this.description = description;
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
