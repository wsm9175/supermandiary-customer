package com.lodong.spring.supermandiarycustomer.enumvalue;

public enum Exchange {
    EXCHANGE("supermandiary.exchange");

    private final String exchange;

    Exchange(String exchange) {
        this.exchange = exchange;
    }

    public String getExchange() {
        return exchange;
    }
}
