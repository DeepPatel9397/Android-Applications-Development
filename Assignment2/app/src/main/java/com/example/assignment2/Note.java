package com.example.assignment2;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable
{
    private String title;
    private String description;
    private Date date;

    public Note()
    {

    }

    public Note(String title, String description, Date date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return title + ", " + description + "," + date;
    }
}