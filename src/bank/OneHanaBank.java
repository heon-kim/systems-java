package bank;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class OneHanaBank {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AccountFactory accountFactory = new AccountFactory();

        Map<String, Account> accounts = new HashMap<>(Map.of(
                "1", accountFactory.createAccount("saving", "1", "자유입출금 통장", "홍길동", 0),
                "2", accountFactory.createAccount("fixed", "2", "정기예금 통장", "홍길동", 50000000),
                "3", accountFactory.createAccount("overdraft", "3", "마이너스 통장", "홍길동", 0)
        ));

        new AccountHandler(scanner, accounts).start();
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
    String getAccountName();
    String getAccountNumber();
    Map<String, String> getActionDescriptions();
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
        if(amount==0) return;
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

class AccountHandler {
    private final Scanner scanner;
    private final Map<String, Account> accounts;

    public AccountHandler(Scanner scanner, Map<String, Account> accounts) {
        this.scanner = scanner;
        this.accounts = accounts;
    }

    public void start() {
        while (true) {
            System.out.print("통장을 선택하세요 "+generateMenuString(accounts));
            String choice = scanner.nextLine();
            if ("0".equals(choice) || choice.trim().isEmpty()) break;
            Account account = accounts.get(choice);
            if (account != null) handleAccount(account);
            else System.out.println("올바른 선택이 아닙니다.");
        }
    }

    private void handleAccount(Account account) {
        account.displayInfo();
        boolean isCompleted = false;
        while (!isCompleted) {
            Map<String, String> actions = account.getActionDescriptions();
            String message = account instanceof FixedDepositAccount?"정기 예금이 만기되었습니다. ":"원하시는 업무는? ";
            System.out.print(message+generateMenuString(actions));
            String action = scanner.nextLine();
            try {
                switch (action) {
                    case "+" -> {
                        if (account instanceof FixedDepositAccount) {
                            isCompleted = handleMature((FixedDepositAccount) account);
                        } else {
                            account.deposit(getAmount("입금 하실 금액은? "));
                        }
                    }
                    case "-" -> handleWithdraw(account);
                    case "T" -> handleTransfer(account);
                    case "I" -> handleDisplayInfo(account);
                    case "0" -> { return; }
                    default -> System.out.println("잘못된 입력입니다.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private <T> String generateMenuString(Map<String, T> map) {
        StringBuilder menu = new StringBuilder("(");
        for (Map.Entry<String, T> entry : map.entrySet()) {
            String valueStr = (entry.getValue() instanceof Account)
                    ? ((Account) entry.getValue()).getAccountName()
                    : entry.getValue().toString();
            menu.append(entry.getKey()).append(": ").append(valueStr).append(", ");
        }
        if (menu.length() > 2) {
            menu.setLength(menu.length() - 2);
        }
        menu.append(") ");
        return menu.toString();
    }

    private String generateAccountTransferMenuString(Account currentAccount) {
        StringBuilder menu = new StringBuilder("(");
        for (Map.Entry<String, Account> entry : accounts.entrySet()) {
            Account targetAccount = entry.getValue();
            if (!targetAccount.getAccountNumber().equals(currentAccount.getAccountNumber())) {
                menu.append(targetAccount.getAccountNumber()).append(": ").append(targetAccount.getAccountName()).append(", ");
            }
        }
        if (menu.length() > 2) {
            menu.setLength(menu.length() - 2);
        }
        menu.append(") ");
        return menu.toString();
    }


    private boolean handleMature(FixedDepositAccount account) throws Exception {
        while (true) {
            int months = getAmount("예치 개월 수를 입력하세요? (1 ~ 60 개월) ");
            if (months == 0) {
                return false;
            };
            account.setDepositMonths(months);
            double interestRate = account.getInterestRate();
            String response = getUserInput(months + "개월(적용금리 " + interestRate + "%)로 만기 처리하시겠어요? (y/n): ");

            if (response.equalsIgnoreCase("Y")) {
                String targetChoice = getUserInput("어디로 보낼까요? "+generateAccountTransferMenuString(account));
                Account target = accounts.get(targetChoice);

                if (target == null) {
                    System.out.println("잘못된 계좌 선택입니다.");
                    continue;
                }

                try {
                    account.matureAccount(target);
                    String accountName = account.getAccountName();
                    accounts.remove(account.getAccountNumber());
                    System.out.println(accountName + "은 해지되었습니다. 감사합니다.");
                    return true;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void handleWithdraw(Account account) throws Exception {
        if (account instanceof FixedDepositAccount) {
            throw new UnsupportedOperationException("출금할 수 없는 통장입니다.");
        }
        while (true) {
            try {
                account.withdraw(getAmount("출금 하실 금액은? "));
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void handleTransfer(Account account) throws Exception {
        if (account instanceof FixedDepositAccount) {
            throw new UnsupportedOperationException("이체할 수 없는 통장입니다.");
        }

        String targetChoice = getUserInput("어디로 보낼까요? "+generateAccountTransferMenuString(account));
        Account targetAccount = accounts.get(targetChoice);

        if (targetAccount != null) {
            while (true) {
                try {
                    int amount = getAmount(targetAccount.getAccountName() + "에 보낼 금액은? ");
                    account.transfer(targetAccount, amount);
                    break;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            System.out.println("잘못된 계좌 선택입니다.");
        }
    }

    private void handleDisplayInfo(Account account) {
        account.displayInfo();
        if (account instanceof FixedDepositAccount) {
            ((FixedDepositAccount) account).displayInterestRates();
        }
    }

    private int getAmount(String message) {
        System.out.print(message);
        int amount = scanner.nextInt();
        scanner.nextLine();
        return amount;
    }

    private String getUserInput(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }
}

class InterestRateCalculator {
    private static final double[] INTEREST_RATES = {3.0, 3.35, 3.4, 3.35, 3.35, 2.9, 2.9, 2.9};
    private static final int[] MONTH_THRESHOLDS = {1, 3, 6, 9, 12, 24, 36, 48, 60};

    public double getInterestRate(int months) {
        for (int i = MONTH_THRESHOLDS.length - 1; i >= 0; i--) {
            if (months >= MONTH_THRESHOLDS[i]) return INTEREST_RATES[i];
        }
        return 0;
    }

    public void printInterestRates() {
        System.out.println("* 예치 개월에 따른 적용 금리");
        for (int i = 0; i < INTEREST_RATES.length; i++) {
            System.out.println("    " + MONTH_THRESHOLDS[i] + "개월 이상    " + INTEREST_RATES[i] + "%");
        }
    }
}

