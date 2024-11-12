package bank;

import java.util.Map;

interface Account {
    void deposit(int amount);
    void withdraw(int amount) throws Exception;
    void transfer(Account target, int amount) throws Exception;
    void displayInfo();
    String getAccountName();
    String getAccountNumber();
    Map<String, String> getActionDescriptions();
}
