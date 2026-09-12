package org.vaelow233.botloom.extensions.whitelist.data;

import java.time.LocalDateTime;

public class WhitelistBind {
    private long id;
    private LocalDateTime createdAt;
    private String userId;
    private String name;

    public String name() {
        return name;
    }

    public String userId() {
        return userId;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public long id() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
