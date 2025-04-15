package com.example.smartspend;

import java.util.Date;

public class Transaction {
    public enum Type { INCOME, EXPENSE }

    private float amount;
    private String category;
    private Type type;
    private Date date;

    public Transaction(float amount, String category, Type type) {
        this.amount = amount;
        this.category = category;
        this.type = type;
        this.date = new Date();
    }

    // Геттеры
    public float getAmount() { return amount; }
    public String getCategory() { return category; }
    public Type getType() { return type; }
    public Date getDate() { return date; }
}