package bank;

import java.util.Map;

class FixedDepositAccount extends BasicAccount {
    private int depositMonths;
    private boolean isMatured;
    private final InterestRateCalculator interestRateCalculator = new InterestRateCalculator();

    public FixedDepositAccount(String accountNumber, String accountName, String ownerName, int balance, int depositMonths) {
        super(accountNumber, accountName, ownerName, balance);
        this.depositMonths = depositMonths;
    }

    public void setDepositMonths(int depositMonths) {
        this.depositMonths = depositMonths;
    }

    public double getInterestRate() {
        return interestRateCalculator.getInterestRate(depositMonths);
    }

    public void matureAccount(Account target) throws Exception {
        if (isMatured) throw new Exception("이미 만기 처리된 계좌입니다.");
        double interestRate = getInterestRate();
        double finalAmount = balance * (1 + interestRate / 100);
        finalAmount = Math.round(finalAmount);
        target.deposit((int) finalAmount);
        balance = 0;
        isMatured = true;
    }

    @Override
    public void deposit(int amount) {
        balance += amount;
        System.out.println(amount + "원이 정기예금 계좌에 입금되었습니다.");
    }

    @Override
    public void withdraw(int amount) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("출금할 수 없는 통장입니다.");
    }

    @Override
    public void transfer(Account target, int amount) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("이체할 수 없는 통장입니다.");
    }

    @Override
    public Map<String, String> getActionDescriptions() {
        return Map.of("+", "만기처리", "-", "인출", "T", "이체", "I", "정보");
    }

    public void displayInterestRates() {
        interestRateCalculator.printInterestRates();
    }
}
