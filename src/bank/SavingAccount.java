package bank;

class SavingAccount extends Account {

    public SavingAccount(String accountNumber, String accountName, String ownerName) {
        super(accountNumber, accountName, ownerName, 0);
    }

    @Override
    public void deposit(int amount) {
        balance += amount;
        System.out.println(accountName + "에 " + amount + "원이 입금되었습니다.");
    }

    @Override
    public void withdraw(int amount) throws Exception {
        if (balance < amount) throw new Exception("잔액이 부족합니다! (잔액: " + balance + "원)");
        balance -= amount;
        System.out.println(accountName + "에서 " + amount + "원이 출금되었습니다.");
        System.out.println(accountName + "의 잔액은 " + balance + "원입니다.");
    }

    @Override
    public void transfer(Account target, int amount) throws Exception {
        if (balance < amount) throw new Exception("잔액이 부족합니다! (잔액: " + balance + "원)");
        balance -= amount;
        target.deposit(amount);
        System.out.println(target.accountName + "에 " + amount + "원이 입금되었습니다.");
        System.out.println(accountName + "의 잔액은 " + balance + "원입니다.");
    }
}
