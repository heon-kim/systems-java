package bank;

import java.util.Map;
import java.util.Scanner;

class AccountHandler {
    private final Scanner scanner;
    private final Map<String, Account> accounts;

    public AccountHandler(Scanner scanner, Map<String, Account> accounts) {
        this.scanner = scanner;
        this.accounts = accounts;
    }

    public void start() {
        while (true) {
            System.out.print("\n>> 통장을 선택하세요 " + generateMenuString(accounts));
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
            String message = account instanceof FixedDepositAccount ? "> 정기 예금이 만기되었습니다. " : "> 원하시는 업무는? ";
            System.out.print(message + generateMenuString(actions));
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
                    case "0" -> {
                        return;
                    }
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
            }
            ;
            account.setDepositMonths(months);
            double interestRate = account.getInterestRate();
            String response = getUserInput(months + "개월(적용금리 " + interestRate + "%)로 만기 처리하시겠어요? (y/n): ");

            if (response.equalsIgnoreCase("Y")) {
                String targetChoice = getUserInput("어디로 보낼까요? " + generateAccountTransferMenuString(account));
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

        String targetChoice = getUserInput("어디로 보낼까요? " + generateAccountTransferMenuString(account));
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
