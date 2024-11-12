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

