package com.proj.function;

public class AccountManager {
    private Integer numberOfAccounts;

    public Integer getNumberOfAccounts() {
        return this.numberOfAccounts;
    }

    public void setNumberOfAccounts(Integer numberOfAccounts) {
        this.numberOfAccounts = numberOfAccounts;
    }

    public AccountManager(Integer numberOfAccounts) {
        this.numberOfAccounts = numberOfAccounts;
    }
}