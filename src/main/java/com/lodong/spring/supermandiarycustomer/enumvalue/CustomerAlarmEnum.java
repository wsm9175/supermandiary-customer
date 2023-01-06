package com.lodong.spring.supermandiarycustomer.enumvalue;

public enum CustomerAlarmEnum {
    RECEIVE_ESTIMATE("receive_estimate");

    private final String label;
    CustomerAlarmEnum (String label) {
        this.label = label;
    }

    public String label(){
        return label;
    }
}
