package ru.bank.consultant.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RequestStatus {
    in_progress,
    bank_selected,
    completed;

    @JsonCreator
    public static RequestStatus fromString(String value) {
        if (value == null) return null;
        return RequestStatus.valueOf(value.toLowerCase());
    }
}