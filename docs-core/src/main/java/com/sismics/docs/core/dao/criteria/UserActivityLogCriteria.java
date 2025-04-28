package com.sismics.docs.core.dao.criteria;

import com.sismics.docs.core.constant.UserActivityLogType;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class UserActivityLogCriteria {
    private String userId;
    private List<UserActivityLogType> types;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public String getUserId() {
        return userId;
    }

    public UserActivityLogCriteria setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public List<UserActivityLogType> getTypes() {
        return types;
    }

    public UserActivityLogCriteria setTypes(List<UserActivityLogType> types) {
        this.types = types;
        return this;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public UserActivityLogCriteria setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public UserActivityLogCriteria setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }
}
