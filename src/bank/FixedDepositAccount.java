package bank;

class FixedDepositAccount extends Account {
    private int depositMonths;
    private boolean isMatured;

    public FixedDepositAccount(String accountNumber, String accountName, String ownerName, int balance, int depositMonths) {
        super(accountNumber, accountName, ownerName, balance);
        this.depositMonths = depositMonths;
        this.isMatured = false;
    }

    public void matureAccount(int months, Account target) throws Exception {
        if (isMatured) throw new Exception("이미 만기 처리된 계좌입니다.");
        depositMonths = months;
        double interestRate = getInterestRate(months);
        int finalAmount = (int) (balance * (1 + interestRate / 100));
        System.out.println("만기 처리가 완료되었습니다. 금액: " + finalAmount + "원, 이체 계좌로 전송됩니다.");
        target.deposit(finalAmount);
        isMatured = true;
        balance = 0;
    }

    @Override
    public void deposit(int amount) {
        System.out.println(amount + "원이 정기예금 계좌에 입금되었습니다.");
        balance += amount;
    }

    @Override
    public void withdraw(int amount) throws Exception {
        throw new Exception("출금할 수 없는 통장입니다.");
    }

    @Override
    public void transfer(Account target, int amount) throws Exception {
        throw new Exception("이체할 수 없는 통장입니다.");
    }

    private double getInterestRate(int months) {
        if (months >= 60) return 2.9;
        if (months >= 48) return 2.9;
        if (months >= 36) return 2.9;
        if (months >= 24) return 2.9;
        if (months >= 12) return 3.35;
        if (months >= 9) return 3.35;
        if (months >= 6) return 3.4;
        if (months >= 3) return 3.35;
        if (months >= 1) return 3.0;
        return 0;
    }
}
