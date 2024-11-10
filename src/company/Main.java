package company;

public class Main {
    public static void main(String[] args) {
        ManagerService manager = new ManagerService();
        manager.addWorker(new PermanentWorker("코난", 3000000));
        manager.addWorker(new PermanentWorker("장미", 3500000));
        manager.addWorker(new TemporaryWorker("알바1", 10000, 80));
        manager.addWorker(new TemporaryWorker("알바2", 15000, 105));
        manager.addWorker(new SalesWorker("판매왕", 5000000, 2000000, 0.07));
        manager.addWorker(new SalesWorker("판매신입", 2200000, 150000, 0.01));
        manager.showAllSalaryInfo();
        manager.showSalaryInfo("코난");
        manager.showTotalSalary();
    }
}
