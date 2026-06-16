package com.example.expensetracker;

public class Transaction {
    private final int id;
    private final String name;
    private final double amount;
    private final String date;
    private final String type;

    public Transaction(int id, String name, double amount, String date, String type) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }

    public int getId()         { return id; }
    public String getName()    { return name; }
    public double getAmount()  { return amount; }
    public String getDate()    { return date; }
    public String getType()    { return type; }
    public boolean isIncome()  { return DatabaseHelper.TYPE_INCOME.equals(type); }
}