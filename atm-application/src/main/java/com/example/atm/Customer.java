
package com.example.atm;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String name;
    private float balance;
    private int pin;
    private List<String> transactionHistory;

    public Customer(String name, float balance,int pin) {
        this.name = name;
        this.balance = balance;
        this.pin = pin;
        this.transactionHistory = new ArrayList<>();
        this.transactionHistory.add("Initial balance: " + balance);
    }

    
    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    public void addTransaction(String transaction) {
        transactionHistory.add(transaction);
    }
}
