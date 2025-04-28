package com.sismics.docs.core.dao.dto;

public class UserActivityStatDto {
    private String date;
    private Long count;

    public String getDate() {
        return date;
    }

    public UserActivityStatDto setDate(String date) {
        this.date = date;
        return this;
    }

    public Long getCount() {
        return count;
    }

    public UserActivityStatDto setCount(Long count) {
        this.count = count;
        return this;
    }
}
