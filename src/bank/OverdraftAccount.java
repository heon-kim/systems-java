package bank;

class OverdraftAccount extends BasicAccount {
    public OverdraftAccount(String accountNumber, String accountName, String ownerName, int balance) {
        super(accountNumber, accountName, ownerName, balance);
    }

    @Override
    public void displayInfo() {
        System.out.println(accountName+" 잔액: "+balance+"원");
    }

    @Override
    public void deposit(int amount) {
        balance += amount;
        System.out.println(accountName + "에 " + amount + "원이 입금되었습니다.");
    }

    @Override
    public void withdraw(int amount) {
        balance -= amount;
        System.out.println(accountName + "에서 " + amount + "원이 출금되었습니다.");
        System.out.println(accountName + "의 잔액은 " + balance + "원입니다.");
    }

    @Override
    public void transfer(Account target, int amount) {
        balance -= amount;
        target.deposit(amount);
    }
}
