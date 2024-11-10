package bank;

import java.util.Scanner;

public class OneHanaBank {
    private static Scanner scanner = new Scanner(System.in);
    private static SavingAccount savingAccount = new SavingAccount("1", "자유입출금 통장", "홍길동");
    private static FixedDepositAccount fixedDepositAccount = new FixedDepositAccount("2", "정기예금 통장", "홍길동", 50000000, 0);
    private static OverdraftAccount overdraftAccount = new OverdraftAccount("3", "마이너스 통장", "홍길동");

    public static void main(String[] args) {
        while (true) {
            System.out.println("통장을 선택하세요 (1:자유입출금, 2:정기예금, 3:마이너스, 엔터: 종료): ");
            String choice = scanner.nextLine();

            if (choice.isEmpty()) break;

            switch (choice) {
                case "1":
                    savingAccount.displayInfo();
                    handleAccount(savingAccount);
                    break;
                case "2":
                    fixedDepositAccount.displayInfo();
                    handleFixedDepositAccount();
                    break;
                case "3":
                    overdraftAccount.displayInfo();
                    handleAccount(overdraftAccount);
                    break;
                default:
                    System.out.println("올바른 선택이 아닙니다.");
            }
        }
        System.out.println("시스템을 종료합니다.");
    }

    private static void handleAccount(Account account) {
        while (true) {
            System.out.println("원하시는 업무는? (+: 입금, -: 출금, T: 이체, I: 정보, 0: 돌아가기): ");
            String action = scanner.nextLine();

            try {
                switch (action) {
                    case "+":
                        System.out.print("입금할 금액: ");
                        int depositAmount = scanner.nextInt();
                        scanner.nextLine();
                        account.deposit(depositAmount);
                        break;
                    case "-":
                        System.out.print("출금할 금액: ");
                        int withdrawAmount = scanner.nextInt();
                        scanner.nextLine();
                        account.withdraw(withdrawAmount);
                        break;
                    case "T":
                        System.out.print("어디로 보낼까요? (2:정기예금, 3:마이너스): ");
                        String targetChoice = scanner.nextLine();
                        Account target = targetChoice.equals("1") ? fixedDepositAccount : overdraftAccount;
                        System.out.print("이체할 금액: ");
                        int transferAmount = scanner.nextInt();
                        scanner.nextLine();
                        account.transfer(target, transferAmount);
                        break;
                    case "I":
                        account.displayInfo();
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("잘못된 입력입니다.");
                }
            } catch (Exception e) {
                System.out.println("오류: " + e.getMessage());
            }
        }
    }

    private static void handleFixedDepositAccount() {
        System.out.println("정기 예금이 만기되었습니다. (+: 만기처리, -: 출금, T: 이체, I: 정보, 0: 돌아가기): ");
        String action = scanner.nextLine();

        try {
            switch (action) {
                case "+":
                    System.out.print("예치 개월 수를 입력하세요? (1 ~ 60 개월) ");
                    int months = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("만기 처리하시겠어요? ");
                    String response = scanner.nextLine();
                    if (response.equalsIgnoreCase("Y")) {
                        System.out.print("어디로 보낼까요? (1: 자유입출금, 3: 마이너스): ");
                        String targetChoice = scanner.nextLine();
                        Account target = targetChoice.equals("1") ? savingAccount : overdraftAccount;
                        fixedDepositAccount.matureAccount(months, target);
                    }
                    break;
                case "-":
                    System.out.println("출금할 수 없는 통장입니다.");
                    break;
                case "T":
                    System.out.println("이체할 수 없는 통장입니다.");
                    break;
                case "I":
                    fixedDepositAccount.displayInfo();
                    System.out.println("* 예치 개월에 따른 적용 금리");
                    System.out.println("1개월 이상    3.0%");
                    System.out.println("3개월 이상    3.35%");
                    System.out.println("6개월 이상    3.4%");
                    System.out.println("9개월 이상    3.35%");
                    System.out.println("12개월 이상    3.35%");
                    System.out.println("24개월 이상    2.9%");
                    System.out.println("36개월 이상    2.9%");
                    System.out.println("48개월 이상    2.9%");
                    System.out.println("60개월 이상    2.9%");
                    break;
                case "0":
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        } catch (Exception e) {
            System.out.println("오류: " + e.getMessage());
        }
    }
}
