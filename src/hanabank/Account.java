package hanabank;

abstract class Account {
    protected String accountNumber;
    protected String accountName;
    protected String ownerName;
    protected int balance;

    public Account(String accountNumber, String accountName, String ownerName, int balance) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public abstract void deposit(int amount);

    public abstract void withdraw(int amount) throws Exception;

    public abstract void transfer(Account target, int amount) throws Exception;

    public void displayInfo() {
        System.out.println(accountName + " (계좌번호: " + accountNumber + ", 예치금: " + balance + "원, 예금주: " + ownerName + ")");
    }
}
