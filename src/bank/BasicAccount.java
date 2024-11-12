package bank;

import java.util.Map;

abstract class BasicAccount implements Account {
    protected String accountNumber;
    protected String accountName;
    protected String ownerName;
    protected int balance;

    public BasicAccount(String accountNumber, String accountName, String ownerName, int balance) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    @Override
    public void displayInfo() {
        System.out.printf("%s (계좌번호: %s, 예치금: %d원, 예금주: %s)%n", accountName, accountNumber, balance, ownerName);
    }

    @Override
    public Map<String, String> getActionDescriptions() {
        return Map.of("+", "입금", "-", "출금", "T", "이체", "I", "정보");
    }

    @Override
    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String getAccountName() {
        return accountName;
    }
}
