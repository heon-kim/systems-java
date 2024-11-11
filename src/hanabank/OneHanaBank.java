package hanabank;

import java.util.Scanner;

public class OneHanaBank {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SavingAccount savingAccount = new SavingAccount("1", "자유입출금 통장", "홍길동");
    private static final FixedDepositAccount fixedDepositAccount = new FixedDepositAccount("2", "정기예금 통장", "홍길동", 50000000, 0);
    private static final OverdraftAccount overdraftAccount = new OverdraftAccount("3", "마이너스 통장", "홍길동");
    private static final AccountHandler accountHandler = new AccountHandler(scanner, savingAccount, fixedDepositAccount, overdraftAccount);

    public static void main(String[] args) {
        while (true) {
            System.out.print("통장을 선택하세요 (1:자유입출금, 2:정기예금, 3:마이너스): ");
            String choice = scanner.nextLine();

            if (choice.isEmpty()) break;

            switch (choice) {
                case "0" -> {
                    System.out.println("금일 OneHanaBank는 업무를 종료합니다. 감사합니다.");
                    return;
                }
                case "1" -> accountHandler.handleAccount(savingAccount);
                case "2" -> accountHandler.handleFixedDepositAccount();
                case "3" -> accountHandler.handleAccount(overdraftAccount);
                default -> System.out.println("올바른 선택이 아닙니다.");
            }
        }
        System.out.println("금일 OneHanaBank는 업무를 종료합니다. 감사합니다.");
    }
}
