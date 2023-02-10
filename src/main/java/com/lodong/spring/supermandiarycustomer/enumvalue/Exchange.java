package com.lodong.spring.supermandiarycustomer.enumvalue;

public enum Exchange {
    EXCHANGE("amq.topic");

    private final String exchange;

    Exchange(String exchange) {
        this.exchange = exchange;
    }

    public String getExchange() {
        return exchange;
    }
}
