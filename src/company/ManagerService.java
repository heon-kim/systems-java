package company;

import java.util.ArrayList;
import java.util.List;

class ManagerService {
    private List<Worker> workers;

    public ManagerService() {
        workers = new ArrayList<>();
    }

    public void addWorker(Worker worker) {
        workers.add(worker);
    }

    public void showAllSalaryInfo() {
        for (Worker worker : workers) {
            worker.showSalaryInfo(worker.name);
        }
    }

    public void showSalaryInfo(String name) {
        for (Worker worker : workers) {
            if (worker.name.equals(name)) {
                worker.showSalaryInfo(name);
                return;
            }
        }
        System.out.println("사원을 찾을 수 없습니다.");
    }

    public void showTotalSalary() {
        int totalSalary = 0;
        for (Worker worker : workers) {
            totalSalary += worker.getPay();
        }
        System.out.println("모든 사원들의 급여 총액은 : " + totalSalary + "원");
    }
}
