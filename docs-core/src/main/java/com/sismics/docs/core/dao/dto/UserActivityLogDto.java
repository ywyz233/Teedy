package com.sismics.docs.core.dao.dto;

import com.sismics.docs.core.constant.UserActivityLogType;

import java.time.LocalDateTime;
import java.util.Date;

public class UserActivityLogDto {
    private String id;

    private String userId;

    private String username;

    private UserActivityLogType action;

    private String documentId;

    private String documentName;

    private LocalDateTime time;

    public String getId() {
        return id;
    }

    public UserActivityLogDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public UserActivityLogDto setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserActivityLogDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserActivityLogType getAction() {
        return action;
    }

    public UserActivityLogDto setAction(UserActivityLogType action) {
        this.action = action;
        return this;
    }

    public String getDocumentId() {
        return documentId;
    }

    public UserActivityLogDto setDocumentId(String documentId) {
        this.documentId = documentId;
        return this;
    }

    public String getDocumentName() {
        return documentName;
    }

    public UserActivityLogDto setDocumentName(String documentName) {
        this.documentName = documentName;
        return this;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public UserActivityLogDto setTime(LocalDateTime time) {
        this.time = time;
        return this;
    }
}



