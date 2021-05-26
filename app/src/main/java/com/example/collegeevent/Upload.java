package com.example.collegeevent;

import java.io.Serializable;

public class Upload implements Serializable {
    public String name;
    public String url;
    public String venue;
    public String dept;
    public String start_time;
    public String date;
    public String link;
    public String desc;
    public String end_time;
    public String key;

    public Upload() {

    }
    public Upload(String name, String url, String venue, String dept, String start_time, String date,String link, String desc, String end_time) {
            this.name = name;
            this.url= url;
            this.venue=venue;
            this.dept=dept;
            this.start_time = start_time;
            this.date = date;
            this.link = link;
            this.desc = desc;
            this.end_time = end_time;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
