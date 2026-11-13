package com.sms.model;

public enum Department {
    COMPUTER_SCIENCE("Computer Science", "CS"),
    INFORMATION_TECHNOLOGY("Information Technology", "IT"),
    MECHANICAL_ENGINEERING("Mechanical Engineering", "ME"),
    CIVIL_ENGINEERING("Civil Engineering", "CE"),
    ELECTRICAL_ENGINEERING("Electrical Engineering", "EE"),
    BUSINESS_ADMINISTRATION("Business Administration", "BA");

    private final String fullName;
    private final String code;

    Department(String fullName, String code) {
        this.fullName = fullName;
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCode() {
        return code;
    }
}
