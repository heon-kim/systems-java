package hanabank;

import java.util.Scanner;

public class AccountHandler {
    private final Scanner scanner;
    private final SavingAccount savingAccount;
    private final FixedDepositAccount fixedDepositAccount;
    private final OverdraftAccount overdraftAccount;

    private static final String DEPOSIT_ACTION = "+";
    private static final String WITHDRAW_ACTION = "-";
    private static final String TRANSFER_ACTION = "T";
    private static final String INFO_ACTION = "I";
    private static final String EXIT_ACTION = "0";

    public AccountHandler(Scanner scanner, SavingAccount savingAccount, FixedDepositAccount fixedDepositAccount, OverdraftAccount overdraftAccount) {
        this.scanner = scanner;
        this.savingAccount = savingAccount;
        this.fixedDepositAccount = fixedDepositAccount;
        this.overdraftAccount = overdraftAccount;
    }

    public void handleAccount(Account account) {
        while (true) {
            String action = getUserInput("원하시는 업무는? (+: 입금, -: 출금, T: 이체, I: 정보): ");

            try {
                switch (action) {
                    case DEPOSIT_ACTION -> deposit(account);
                    case WITHDRAW_ACTION -> withdraw(account);
                    case TRANSFER_ACTION -> transfer(account);
                    case INFO_ACTION -> account.displayInfo();
                    case EXIT_ACTION -> { return; }
                    default -> System.out.println("잘못된 입력입니다.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void handleFixedDepositAccount() {
        String action = getUserInput("정기 예금이 만기되었습니다. (+: 만기처리, -: 출금, T: 이체, I: 정보): ");

        try {
            switch (action) {
                case DEPOSIT_ACTION -> processMatureFixedDeposit();
                case WITHDRAW_ACTION -> System.out.println("출금할 수 없는 통장입니다.");
                case TRANSFER_ACTION -> System.out.println("이체할 수 없는 통장입니다.");
                case INFO_ACTION -> displayFixedDepositInfo();
                case EXIT_ACTION -> { return; }
                default -> System.out.println("잘못된 입력입니다.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void deposit(Account account) {
        int amount = getAmount("입금 하실 금액은? ");
        account.deposit(amount);
    }

    private void withdraw(Account account) throws Exception {
        int amount = getAmount("출금 하실 금액은? ");
        account.withdraw(amount);
    }

    private void transfer(Account account) throws Exception {
        String targetChoice = getUserInput("어디로 보낼까요? (2:정기예금, 3:마이너스): ");
        Account target = getTransferTarget(targetChoice);
        int amount = getAmount(target.accountName + "에 보낼 금액은? ");
        account.transfer(target, amount);
    }

    private void processMatureFixedDeposit() throws Exception {
        int months = getAmount("예치 개월 수를 입력하세요? (1 ~ 60 개월) ");
        String response = getUserInput("만기 처리하시겠어요? (Y/N): ");
        if (response.equalsIgnoreCase("Y")) {
            String targetChoice = getUserInput("어디로 보낼까요? (1: 자유입출금, 3: 마이너스): ");
            Account target = getTransferTarget(targetChoice);
            fixedDepositAccount.matureAccount(months, target);
        }
    }

    private void displayFixedDepositInfo() {
        fixedDepositAccount.displayInfo();
        System.out.println("* 예치 개월에 따른 적용 금리");
        System.out.println("    1개월 이상    3.0%");
        System.out.println("    3개월 이상    3.35%");
        System.out.println("    6개월 이상    3.4%");
        System.out.println("    9개월 이상    3.35%");
        System.out.println("    12개월 이상    3.35%");
        System.out.println("    24개월 이상    2.9%");
        System.out.println("    36개월 이상    2.9%");
        System.out.println("    48개월 이상    2.9%");
        System.out.println("    60개월 이상    2.9%");
    }

    private String getUserInput(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    private int getAmount(String message) {
        System.out.print(message);
        int amount = scanner.nextInt();
        scanner.nextLine();
        return amount;
    }

    private Account getTransferTarget(String targetChoice) {
        return switch (targetChoice) {
            case "1" -> savingAccount;
            case "2" -> fixedDepositAccount;
            case "3" -> overdraftAccount;
            default -> throw new IllegalArgumentException("잘못된 계좌 선택입니다.");
        };
    }
}
