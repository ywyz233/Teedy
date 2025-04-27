package com.sismics.docs.core.dao.dto;

public class LoginStatisticsDto {

    private String date;
    private Long loginCount;

    public LoginStatisticsDto() {
    }

    public String getDate() {
        return date;
    }

    public LoginStatisticsDto setDate(String date) {
        this.date = date;
        return this;
    }

    public Long getLoginCount() {
        return loginCount;
    }

    public LoginStatisticsDto setLoginCount(Long loginCount) {
        this.loginCount = loginCount;
        return this;
    }
}
