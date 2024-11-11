package hanabank;

class FixedDepositAccount extends Account {
    private int depositMonths;
    private boolean isMatured;

    private static final double[] INTEREST_RATES = {3.0, 3.35, 3.4, 3.35, 3.35, 2.9, 2.9, 2.9};
    private static final int[] MONTH_THRESHOLDS = {1, 3, 6, 9, 12, 24, 36, 48, 60};

    public FixedDepositAccount(String accountNumber, String accountName, String ownerName, int balance, int depositMonths) {
        super(accountNumber, accountName, ownerName, balance);
        this.depositMonths = depositMonths;
        this.isMatured = false;
    }

    public void matureAccount(int months, Account target) throws Exception {
        if (isMatured) throw new Exception("이미 만기 처리된 계좌입니다.");
        depositMonths = months;

        int finalAmount = calculateMaturityAmount();
        target.deposit(finalAmount);
        isMatured = true;
        balance = 0;
        System.out.println(accountName+"은 해지되었습니다. 감사합니다.");
    }

    private int calculateMaturityAmount() {
        double interestRate = getInterestRate(depositMonths);
        return (int) (balance * (1 + interestRate / 100));
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
        for (int i = MONTH_THRESHOLDS.length - 1; i >= 0; i--) {
            if (months >= MONTH_THRESHOLDS[i]) {
                return INTEREST_RATES[i];
            }
        }
        return 0;
    }
}
