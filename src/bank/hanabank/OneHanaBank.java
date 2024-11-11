package bank.hanabank;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class OneHanaBank {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AccountFactory accountFactory = new AccountFactory();

        Map<String, Account> accounts = new HashMap<>();
        accounts.put("1", accountFactory.createAccount("saving", "1", "자유입출금 통장", "홍길동", 0));
        accounts.put("2", accountFactory.createAccount("fixed", "2", "정기예금 통장", "홍길동", 50000000));
        accounts.put("3", accountFactory.createAccount("overdraft", "3", "마이너스 통장", "홍길동", 0));

        AccountHandler accountHandler = new AccountHandler(scanner, accounts);

        accountHandler.start();
        System.out.println("금일 OneHanaBank는 업무를 종료합니다. 감사합니다.");
        scanner.close();
    }
}

class AccountFactory {
    public Account createAccount(String type, String accountNumber, String accountName, String ownerName, int balance) {
        return switch (type) {
            case "saving" -> new SavingAccount(accountNumber, accountName, ownerName, balance);
            case "fixed" -> new FixedDepositAccount(accountNumber, accountName, ownerName, balance, 0);
            case "overdraft" -> new OverdraftAccount(accountNumber, accountName, ownerName, balance);
            default -> throw new IllegalArgumentException("Unknown account type");
        };
    }
}

interface Account {
    void deposit(int amount);
    void withdraw(int amount) throws Exception;
    void transfer(Account target, int amount) throws Exception;
    void displayInfo();
    String getAccountName();  // 계좌 이름 반환 메서드 추가
}

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
        System.out.println(accountName + " (계좌번호: " + accountNumber + ", 예치금: " + balance + "원, 예금주: " + ownerName + ")");
    }

    @Override
    public String getAccountName() {  // 계좌 이름 반환 메서드 구현
        return accountName;
    }
}

class SavingAccount extends BasicAccount {
    public SavingAccount(String accountNumber, String accountName, String ownerName, int balance) {
        super(accountNumber, accountName, ownerName, balance);
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
    }

    @Override
    public void transfer(Account target, int amount) throws Exception {
        if (balance < amount) throw new Exception("잔액이 부족합니다! (잔액: " + balance + "원)");
        balance -= amount;
        target.deposit(amount);
        System.out.println(accountName + "의 잔액은 " + balance + "원입니다.");
    }
}

class FixedDepositAccount extends BasicAccount {
    private final InterestRateCalculator interestRateCalculator = new InterestRateCalculator();
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

        int finalAmount = calculateMaturityAmount();
        target.deposit(finalAmount);
        isMatured = true;
        balance = 0;
        System.out.println(accountName + "은 해지되었습니다. 감사합니다.");
    }

    private int calculateMaturityAmount() {
        double interestRate = interestRateCalculator.getInterestRate(depositMonths);
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
}

class OverdraftAccount extends BasicAccount {
    public OverdraftAccount(String accountNumber, String accountName, String ownerName, int balance) {
        super(accountNumber, accountName, ownerName, balance);
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
    }

    @Override
    public void transfer(Account target, int amount) {
        balance -= amount;
        target.deposit(amount);
        System.out.println(accountName + "의 잔액은 " + balance + "원입니다.");
    }
}

class AccountHandler {
    private final Scanner scanner;
    private final Map<String, Account> accounts;

    public AccountHandler(Scanner scanner, Map<String, Account> accounts) {
        this.scanner = scanner;
        this.accounts = accounts;
    }

    public void start() {
        while (true) {
            System.out.print("통장을 선택하세요 (1:자유입출금, 2:정기예금, 3:마이너스): ");
            String choice = scanner.nextLine();

            if (choice.equals("0")) break;

            Account account = accounts.get(choice);
            if (account == null) {
                System.out.println("올바른 선택이 아닙니다.");
                continue;
            }

            handleAccount(account);
        }
    }

    private void handleAccount(Account account) {
        while (true) {
            System.out.print("원하시는 업무는? (+: 입금, -: 출금, T: 이체, I: 정보, 0: 종료): ");
            String action = scanner.nextLine();

            try {
                switch (action) {
                    case "+" -> account.deposit(getAmount("입금 하실 금액은? "));
                    case "-" -> account.withdraw(getAmount("출금 하실 금액은? "));
                    case "T" -> handleTransfer(account);
                    case "I" -> account.displayInfo();
                    case "0" -> { return; }
                    default -> System.out.println("잘못된 입력입니다.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void handleTransfer(Account account) throws Exception {
        System.out.print("어디로 보낼까요? (1:자유입출금, 2:정기예금, 3:마이너스): ");
        String targetChoice = scanner.nextLine();
        Account targetAccount = accounts.get(targetChoice);

        if (targetAccount != null) {
            int amount = getAmount(targetAccount.getAccountName() + "에 보낼 금액은? ");
            account.transfer(targetAccount, amount);
        } else {
            System.out.println("잘못된 계좌 선택입니다.");
        }
    }

    private int getAmount(String message) {
        System.out.print(message);
        int amount = scanner.nextInt();
        scanner.nextLine();
        return amount;
    }
}

class InterestRateCalculator {
    private static final double[] INTEREST_RATES = {3.0, 3.35, 3.4, 3.35, 3.35, 2.9, 2.9, 2.9};
    private static final int[] MONTH_THRESHOLDS = {1, 3, 6, 9, 12, 24, 36, 48, 60};

    public double getInterestRate(int months) {
        for (int i = MONTH_THRESHOLDS.length - 1; i >= 0; i--) {
            if (months >= MONTH_THRESHOLDS[i]) {
                return INTEREST_RATES[i];
            }
        }
        return 0;
    }
}
