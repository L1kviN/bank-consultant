package ru.bank.consultant.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    user,
    admin;

    @JsonCreator
    public static Role fromString(String value) {
        if (value == null) return null;
        return Role.valueOf(value.toLowerCase());
    }
}