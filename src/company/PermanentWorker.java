package company;

class PermanentWorker extends Worker {
    private int salary;

    public PermanentWorker(String name, int salary) {
        super(name);
        this.salary = salary;
    }

    public int getPay() {
        return salary;
    }

    public void showSalaryInfo(String name) {
        System.out.println("사원 " + name + "의 급여는 " + salary + "원");
    }
}
