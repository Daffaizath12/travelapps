package com.example.travelapps.Model;

import java.util.Date;

public class Notifikasi {
    private String title;
    private String desc;
    private Date createdAt;

    public Notifikasi(String title, String desc, Date createdAt) {
        this.title = title;
        this.desc = desc;
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
