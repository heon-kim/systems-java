package company;

class SalesWorker extends PermanentWorker {
    private int salesAmount;
    private double bonusRatio;

    public SalesWorker(String name, int salary, int salesAmount, double bonusRatio) {
        super(name, salary);
        this.salesAmount = salesAmount;
        this.bonusRatio = bonusRatio;
    }

    public int getPay() {
        return super.getPay() + (int) (salesAmount * bonusRatio);
    }

    public void showSalaryInfo(String name) {
        int baseSalary = super.getPay();
        int bonus = (int) (salesAmount * bonusRatio);
        System.out.println("사원 " + name + "의 급여는 월급 " + baseSalary + "원, 수당 " + bonus + "원을 합한 총액 " + getPay() + "원");
    }
}
