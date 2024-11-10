package company;

class TemporaryWorker extends Worker {
    private int payPerHour;
    private int workTime;

    public TemporaryWorker(String name, int payPerHour, int workTime) {
        super(name);
        this.payPerHour = payPerHour;
        this.workTime = workTime;
    }

    public int getPay() {
        return payPerHour * workTime;
    }

    public void showSalaryInfo(String name) {
        System.out.println("사원 " + name + "의 근무시간은 " + workTime + "시간, 시간 수당은 " + payPerHour + "원 급여는 " + getPay() + "원");
    }
}
