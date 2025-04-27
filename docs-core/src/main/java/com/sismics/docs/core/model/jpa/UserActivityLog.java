package com.sismics.docs.core.model.jpa;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "T_USER_ACTIVITY_LOG")
public class UserActivityLog {

    @Id
    @Column(name = "UAL_ID_C", length = 36)
    private String id;

    @Column(name = "UAL_USERID_C", length = 36)
    private String userId;

    @Column(name = "UAL_USERNAME_C", length = 50)
    private String username;

    @Column(name = "UAL_ACTION_N")
    private Integer action;

    @Column(name = "UAL_DOCUMENT_ID_C", length = 36)
    private String documentId;

    @Column(name = "UAL_DOCUMENT_NAME_C", length = 255)
    private String documentName;

    @Column(name = "UAL_TIME_D")
    private LocalDateTime time;

    public String getId() {
        return id;
    }

    public UserActivityLog setId(String id) {
        this.id = id;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public UserActivityLog setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserActivityLog setUsername(String username) {
        this.username = username;
        return this;
    }

    public Integer getAction() {
        return action;
    }

    public UserActivityLog setAction(Integer action) {
        this.action = action;
        return this;
    }

    public String getDocumentId() {
        return documentId;
    }

    public UserActivityLog setDocumentId(String documentId) {
        this.documentId = documentId;
        return this;
    }

    public String getDocumentName() {
        return documentName;
    }

    public UserActivityLog setDocumentName(String documentName) {
        this.documentName = documentName;
        return this;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public UserActivityLog setTime(LocalDateTime time) {
        this.time = time;
        return this;
    }
}
