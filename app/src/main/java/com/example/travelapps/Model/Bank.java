package com.example.travelapps.Model;

public class Bank {
    private String bank;
    private String vaNumber;

    // Constructor
    public Bank(String bank, String vaNumber) {
        this.bank = bank;
        this.vaNumber = vaNumber;
    }

    // Getter methods
    public String getBank() {
        return bank;
    }

    public String getVaNumber() {
        return vaNumber;
    }
}
