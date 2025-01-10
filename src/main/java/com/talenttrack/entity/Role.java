package com.talenttrack.entity;

public enum Role {
    ADMIN,
    SUPER_ADMIN,
    MODERATOR,
    CONTENT_MANAGER,
    USER,
    GUEST,
    REGISTERED_USER,
    PREMIUM_USER,
    MANAGER,
    TEAM_LEAD,
    EMPLOYEE,
    INTERN,
    CUSTOMER,
    SELLER,
    SUPPLIER,
    DISTRIBUTOR,
    SYSTEM_ADMIN,
    DEVOPS_ENGINEER,
    TECH_SUPPORT,
    STUDENT,
    TEACHER,
    PRINCIPAL,
    LIBRARIAN,
    PROJECT_MANAGER,
    QUALITY_ANALYST,
    DATA_ANALYST;

    public String getRoleName() {
        return "ROLE_" + name(); 
    }
}
