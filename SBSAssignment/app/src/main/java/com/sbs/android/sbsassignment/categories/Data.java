package com.sbs.android.sbsassignment.categories;


/*
* A simple POJO class to deserialize the data retrieved from JSON in APIService class.
* */
public class Data {
    private String Id;
    private String slug;
    private String title;
    private String color;
    private String order;

    public void setId(String id) {
        Id = id;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getId() {
        return Id;
    }

    public String getSlug() {
        return slug;
    }

    public String getTitle() {
        return title;
    }

    public String getColor() {
        return color;
    }

    public String getOrder() {
        return order;
    }
}
