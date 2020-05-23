package com.example.mycostcalendar;

public enum Constants {

    COST_TITLE("com.example.mycostcalendar.COST_TITLE"),
    COST_PRICE("com.example.mycostcalendar.COST_PRICE"),
    COST_DESCRIPTION("com.example.mycostcalendar.COST_DESCRIPTION"),
    IS_EDIT("com.example.mycostcalendar.IS_EDIT"),
    COST_EXTRA("com.example.mycostcalendar.COST_EXTRA"),
    INCOME_DATE("com.example.mycostcalendar.INCOME_DATE");

    private String value;

    private Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
