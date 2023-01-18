package com.lodong.spring.supermandiarycustomer.enumvalue;

public enum ConstructorAlarmEnum {
    RECEIVE_REQUEST_ORDER("request_order"),
    REJECT_ESTIMATE("reject_estimate");
    private final String label;
    ConstructorAlarmEnum (String label) {
        this.label = label;
    }

    public String label(){
        return label;
    }
}
