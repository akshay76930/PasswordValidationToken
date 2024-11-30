package com.talenttrack.entity;

public enum Role {
    ADMIN,
    USER;
    

    public String getRoleName() {
        return "ROLE_" + name(); 
    }
}
