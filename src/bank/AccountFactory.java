package bank;

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
